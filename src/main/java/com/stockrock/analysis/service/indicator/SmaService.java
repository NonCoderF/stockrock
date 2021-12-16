package com.stockrock.analysis.service.indicator;

import com.stockrock.analysis.constants.CommandConstants;
import com.stockrock.analysis.constants.CommandConstants.Indicators;
import com.stockrock.analysis.constants.MessageStatus;
import com.stockrock.analysis.model.Data;
import com.stockrock.analysis.model.Message;
import com.stockrock.analysis.model.indicators.Sma;
import com.stockrock.analysis.service.ServiceInterface;
import org.springframework.stereotype.Service;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class SmaService extends ServiceInterface {

    @Override
    public void processSmaIndicator(Data data, int[] barCounts, BarSeries series) {
        super.processSmaIndicator(data, barCounts, series);

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        HashMap<String, List<Double>> smaIndicators = new HashMap<>();

        for (int barCount : barCounts) {
            SMAIndicator shortSma = new SMAIndicator(closePrice, barCount);

            Sma sma = new Sma();
            sma.setBarCount(barCount);
            List<Double> smas = new ArrayList<>();
            for (int i = 0; i < series.getBarCount(); i++) {
                smas.add(shortSma.getValue(i).doubleValue());
            }
            sma.setSma(smas);

            smaIndicators.put(Indicators.sma.name() + "_" + barCount, sma.getSma());
        }

        data.getIndicatorSeries().putAll(smaIndicators);

        getObserver().observe(CommandConstants.Services.smaService.name(), data.getSessionId(), data, MessageStatus.OKAY);

    }
}
