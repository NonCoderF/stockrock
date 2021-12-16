package com.stockrock.analysis.service.indicator;

import com.stockrock.analysis.constants.CommandConstants;
import com.stockrock.analysis.constants.CommandConstants.Indicators;
import com.stockrock.analysis.constants.MessageStatus;
import com.stockrock.analysis.model.Data;
import com.stockrock.analysis.service.ServiceInterface;
import org.springframework.stereotype.Service;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class EmaService extends ServiceInterface {

    @Override
    public void processEmaIndicator(Data data, int[] barCounts, BarSeries series) {
        super.processEmaIndicator(data, barCounts, series);

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        HashMap<String, List<Double>> emaIndicators = new HashMap<>();

        for (int barCount : barCounts) {
            EMAIndicator shortEma = new EMAIndicator(closePrice, barCount);

            List<Double> ema = new ArrayList<>();

            for (int i = 0; i < series.getBarCount(); i++) {
                ema.add(shortEma.getValue(i).doubleValue());
            }

            emaIndicators.put(Indicators.ema.name() + "_" + barCount, ema);
        }

        data.getIndicatorSeries().putAll(emaIndicators);

        getObserver().observe(CommandConstants.Services.emaService.name(), data.getSessionId(), data, MessageStatus.OKAY);

    }
}
