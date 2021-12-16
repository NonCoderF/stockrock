package com.stockrock.analysis.loaders;

import com.stockrock.analysis.model.Data;
import com.stockrock.analysis.model.OhlcSeries;
import com.stockrock.analysis.network.ApiCall;
import com.stockrock.analysis.utils.SeriesUtils;
import com.stockrock.analysis.utils.SysOut;
import com.stockrock.analysis.utils.TimeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TwelveDataFinanceLoader implements ApiCall.ApiListener {

    TwelveDataFinanceLoaderListener listener;

    String from, to, interval;

    Data data;

    public static TwelveDataFinanceLoader getInstance() {
        return new TwelveDataFinanceLoader();
    }

    public void loadSeries(Data data, TwelveDataFinanceLoaderListener listener) {

        this.data = data;

        this.listener = listener;

        this.from = data.getFrom();
        this.to = data.getTo();
        this.interval = data.getInterval();

        String toDate = new SimpleDateFormat("yyyy-MM-dd").format(TimeUtils.parseStringTime(to));
        String fromDate = new SimpleDateFormat("yyyy-MM-dd").format(TimeUtils.parseStringTime(from));

        String url = "https://api.twelvedata.com/time_series?"
                + "start_date=" + fromDate
                + "&end_date=" + toDate
                + "&order=ASC"
                + "&interval=" + interval
                + "&symbol=" + data.getSymbol()
                + "&apikey=b7104eeaccc441bdb834f6033b95285f";

        SysOut.d(url);

        ApiCall.Builder builder = ApiCall.Builder().setUrl(url).setListener(this);
        ApiCall apiCall = new ApiCall(builder);
        apiCall.connect();
    }

    @Override
    public void onSuccess(String responseString) {
        SysOut.d(responseString);
        try {
            JSONObject object = new JSONObject(responseString);
            if (object.has("status")){
                if (object.getString("status").equalsIgnoreCase("error")){
                    listener.onFailed(object.getString("message"));
                }
            }
            String name = object.getJSONObject("meta").getString("symbol");

            JSONArray array = object.getJSONArray("values");

            //SIMULATING REALTIME
            //if to is not current then make too current
            //Call api and uto current time and push
            //Start socket with api for upcoming datas
            //Close socket is current session is not valid
            int originalLength = array.length();
            int newLength = (int) (originalLength * 0.01);

            if (timer != null) timer.interrupt();

            if (data.getRealtime()) {
                JSONArray array1 = new JSONArray();
                for (int i = 0; i < newLength; i++ ){
                    array1.put(array.get(i));
                }
                pushData(array1, name);

                loopThread(array, array1, newLength, originalLength, name);

            } else {
                pushData(array, name);
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }

    Thread timer;

    private void loopThread(JSONArray array, JSONArray array1, int newLength, int originalLength, String name){
        timer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    for (int i = newLength; i < originalLength; i++){
                        array1.put(array.get(i));
                        pushData(array1, name);
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timer.start();
    }

    private void pushData(JSONArray array, String name) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        BarSeries barSeries = new BaseBarSeries(name);
        for (int i = 0; i < array.length(); i++) {
            String datetime = array.getJSONObject(i).getString("datetime");
            if (datetime.length() < 14) {
                datetime += " 00:00:00";
            }
            barSeries.addBar(
                    ZonedDateTime.parse(
                            datetime, formatter.withZone(ZoneId.systemDefault())
                    ),
                    array.getJSONObject(i).getDouble("open"),
                    array.getJSONObject(i).getDouble("high"),
                    array.getJSONObject(i).getDouble("low"),
                    array.getJSONObject(i).getDouble("close"),
                    array.getJSONObject(i).getDouble("volume")
            );

        }
        OhlcSeries series = SeriesUtils.barSeriesToOhlc(barSeries);

        series.setBeginTime(from);
        series.setEndTime(to);
        series.setInterval(interval);

        listener.onReady(series);
    }

    @Override
    public void onFailure(Exception e) {
        SysOut.d(e.getMessage());
        listener.onFailed(e.getMessage());
    }

    public interface TwelveDataFinanceLoaderListener {
        void onReady(OhlcSeries ohlcSeries);

        void onFailed(String error);
    }

}
