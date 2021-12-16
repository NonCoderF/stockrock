package com.stockrock.analysis.utils;

import com.stockrock.analysis.model.OhlcSeries;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;

import java.sql.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class SeriesUtils {

    public static BarSeries ohlcToBarSeries(OhlcSeries series) {
        BarSeries barSeries = new BaseBarSeries();
        try {
            for (int i = 0; i < series.getOpen().size(); i++) {
                barSeries.addBar(
                        ZonedDateTime.ofInstant(TimeUtils.parseStringTime(series.getEndTimeSeries().get(i)).toInstant(), ZoneId.systemDefault()),
                        series.getOpen().get(i),
                        series.getHigh().get(i),
                        series.getLow().get(i),
                        series.getClose().get(i),
                        series.getVolume().get(i)
                );
            }
        }catch (Exception e){
            SysOut.e(e.getMessage());
        }

        return barSeries;
    }

    public static OhlcSeries barSeriesToOhlc(BarSeries barSeries){
        List<Double> open = new ArrayList<>();
        List<Double> close = new ArrayList<>();
        List<Double> high = new ArrayList<>();
        List<Double> low = new ArrayList<>();
        List<Double> volume = new ArrayList<>();
        List<String> begin = new ArrayList<>();
        List<String> end = new ArrayList<>();
        List<Long> duration = new ArrayList<>();

        for (int i = 0; i < barSeries.getBarData().size(); i++){
            open.add(barSeries.getBarData().get(i).getOpenPrice().doubleValue());
            close.add(barSeries.getBarData().get(i).getClosePrice().doubleValue());
            high.add(barSeries.getBarData().get(i).getHighPrice().doubleValue());
            low.add(barSeries.getBarData().get(i).getLowPrice().doubleValue());
            volume.add(barSeries.getBarData().get(i).getVolume().doubleValue());
            begin.add(Date.from(barSeries.getBarData().get(i).getBeginTime().toInstant()).toString());
            end.add(Date.from(barSeries.getBarData().get(i).getEndTime().toInstant()).toString());
            duration.add(barSeries.getBarData().get(i).getTimePeriod().toMillis());
        }

        OhlcSeries ohlcSeries = new OhlcSeries();
        ohlcSeries.setOpen(open);
        ohlcSeries.setClose(close);
        ohlcSeries.setHigh(high);
        ohlcSeries.setLow(low);
        ohlcSeries.setVolume(volume);
        ohlcSeries.setBeginTimeSeries(begin);
        ohlcSeries.setEndTimeSeries(end);
        ohlcSeries.setDuration(duration);
        return ohlcSeries;
    }

}
