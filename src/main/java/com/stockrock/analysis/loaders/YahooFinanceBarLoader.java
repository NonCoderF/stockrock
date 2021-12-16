package com.stockrock.analysis.loaders;

import com.stockrock.analysis.model.OhlcSeries;
import com.stockrock.analysis.network.ApiCall;
import com.stockrock.analysis.utils.SeriesUtils;
import com.stockrock.analysis.utils.SysOut;
import com.stockrock.analysis.utils.TimeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class YahooFinanceBarLoader implements ApiCall.ApiListener {

    YahooFinanceBarsListener listener;

    String from, to, interval;

    public static YahooFinanceBarLoader getInstance() {
        return new YahooFinanceBarLoader();
    }

    public void loadSeries(String symbol, String from, String to, String interval, YahooFinanceBarsListener listener){

        this.listener = listener;

        this.from = from;
        this.to = to;
        this.interval = interval;

        long toMillis = TimeUtils.parseStringTime(to).getTime()/1000;
        long fromMillis = TimeUtils.parseStringTime(from).getTime()/1000;

        String url = "https://query1.finance.yahoo.com/v8/finance/chart/"
                + symbol + "?period1="
                + fromMillis + "&period2="
                + toMillis + "&interval="
                + interval + "&includePrePost=true&events=div%2Csplit";

        SysOut.d("Url : " + url);

        ApiCall.Builder builder = ApiCall.Builder().setUrl(url).setListener(this);
        ApiCall apiCall = new ApiCall(builder);
        apiCall.connect();
    }

    @Override
    public void onSuccess(String responseString) {
        SysOut.d(responseString);
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            JSONObject ohlc = jsonObject.getJSONObject("chart")
                    .getJSONArray("result")
                    .getJSONObject(0)
                    .getJSONObject("indicators")
                    .getJSONArray("quote")
                    .getJSONObject(0);

            JSONArray time = jsonObject.getJSONObject("chart")
                    .getJSONArray("result")
                    .getJSONObject(0)
                    .getJSONArray("timestamp");
            JSONArray open = ohlc.getJSONArray("open");
            JSONArray close = ohlc.getJSONArray("close");
            JSONArray low = ohlc.getJSONArray("low");
            JSONArray high = ohlc.getJSONArray("high");
            JSONArray volume = ohlc.getJSONArray("volume");

            BarSeries barSeries = new BaseBarSeries(jsonObject.getJSONObject("chart")
                    .getJSONArray("result")
                    .getJSONObject(0)
                    .getJSONObject("meta")
                    .getString("symbol"));

            for (int i = 0; i < time.length(); i++){
                long timeS = time.getLong(i);

                ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochSecond(timeS),
                        ZoneId.systemDefault());

                double openD = open.get(i).toString() == "null" ? barSeries.getBar(i - 1).getOpenPrice().doubleValue() : open.getDouble(i) ;
                double highD = high.get(i).toString() == "null" ? barSeries.getBar(i - 1).getHighPrice().doubleValue() : high.getDouble(i) ;
                double closeD = close.get(i).toString() == "null" ? barSeries.getBar(i - 1).getClosePrice().doubleValue() : close.getDouble(i) ;
                double lowD = low.get(i).toString() == "null" ? barSeries.getBar(i - 1).getLowPrice().doubleValue() : low.getDouble(i) ;
                double volumeD = volume.get(i).toString() == "null" ? barSeries.getBar(i - 1).getVolume().doubleValue() : volume.getDouble(i) ;

                barSeries.addBar(zdt, openD, highD, lowD, closeD, volumeD);
            }

            OhlcSeries series = SeriesUtils.barSeriesToOhlc(barSeries);

            series.setBeginTime(from);
            series.setEndTime(to);
            series.setInterval(interval);

            listener.onReady(series);

        }catch (Exception e){
            SysOut.d(e.getMessage());
            listener.onFailed(e.getMessage());
        }
    }

    @Override
    public void onFailure(Exception e) {
        SysOut.d(e.getMessage());
        listener.onFailed(e.getMessage());
    }

    public static boolean isNumeric(Object str) {
        try {
            Double.parseDouble((String) str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public interface YahooFinanceBarsListener{
        void onReady(OhlcSeries ohlcSeries);
        void onFailed(String error);
    }
}
