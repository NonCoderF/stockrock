package com.stockrock.analysis.service.indicator;

import com.stockrock.analysis.constants.CommandConstants;
import com.stockrock.analysis.constants.CommandConstants.Indicators;
import com.stockrock.analysis.constants.MessageStatus;
import com.stockrock.analysis.model.Data;
import com.stockrock.analysis.service.ServiceInterface;
import org.springframework.stereotype.Service;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class RsiService extends ServiceInterface {

    @Override
    public void processRSIIndicator(Data data, int[] barCounts, BarSeries series) {
        super.processRSIIndicator(data, barCounts, series);

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        HashMap<String, List<Double>> rsiIndicators = new HashMap<>();

        for (int barCount : barCounts) {
            RSIIndicator shortRsi = new RSIIndicator(closePrice, barCount);

            List<Double> rsi = new ArrayList<>();

            for (int i = 0; i < series.getBarCount(); i++) {
                rsi.add(shortRsi.getValue(i).doubleValue());
            }

            rsiIndicators.put(Indicators.rsi.name() + "_" + barCount, rsi);
        }

        data.getIndicatorSeries().putAll(rsiIndicators);

        getObserver().observe(CommandConstants.Services.rsiService.name(), data.getSessionId(), data, MessageStatus.OKAY);

    }
}
