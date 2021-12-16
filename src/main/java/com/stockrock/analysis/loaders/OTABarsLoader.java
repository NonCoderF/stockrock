package com.stockrock.analysis.loaders;

import com.stockrock.analysis.model.OhlcSeries;
import com.stockrock.analysis.utils.SeriesUtils;
import com.stockrock.analysis.utils.SysOut;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;

import java.time.format.DateTimeFormatter;

public class OTABarsLoader {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void loadSeries(String symbol, String from, String to, String interval, OTABarsListener listener){
        BarSeries barSeries = new BaseBarSeries(symbol);

        barSeries = CsvBarsLoader.loadAppleIncSeries();

        try {
            Thread.sleep(20);

            OhlcSeries series = SeriesUtils.barSeriesToOhlc(barSeries);

            SysOut.d(from);

            series.setBeginTime(from);
            series.setEndTime(to);
            series.setInterval(interval);

            listener.onReady(series);

        } catch (InterruptedException e) {
            SysOut.e(e.getMessage());
        }

    }

    public interface OTABarsListener{
        void onReady(OhlcSeries ohlcSeries);
        void onFailed(String error);
    }

}
