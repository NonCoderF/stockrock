package com.stockrock.analysis.service.indicator;

import com.stockrock.analysis.constants.CommandConstants;
import com.stockrock.analysis.constants.CommandConstants.Indicators;
import com.stockrock.analysis.constants.MessageStatus;
import com.stockrock.analysis.model.Data;
import com.stockrock.analysis.model.indicators.Sma;
import com.stockrock.analysis.service.ServiceInterface;
import com.stockrock.analysis.utils.SysOut;
import org.springframework.stereotype.Service;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class MacdService extends ServiceInterface {

    @Override
    public void processMACDIndicator(Data data, int[] barCounts, BarSeries series) {
        super.processMACDIndicator(data, barCounts, series);

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        HashMap<String, List<Double>> macdIndicators = new HashMap<>();

        int[] newBarCount;

        if (barCounts.length % 2 == 1){
            newBarCount = Arrays.copyOf(barCounts, barCounts.length - 1);
        }else{
            newBarCount = barCounts;
        }

        for (int i = 0; i < newBarCount.length; i+=2) {
            MACDIndicator shortMacd = new MACDIndicator(closePrice, newBarCount[i], newBarCount[i + 1]);

            List<Double> macd = new ArrayList<>();
            for (int j = 0; j < series.getBarCount(); j++) {
                macd.add(shortMacd.getValue(j).doubleValue());
            }

            macdIndicators.put(Indicators.macd.name() + "_" + newBarCount[i] + "" + newBarCount[i + 1], macd);
        }

        data.getIndicatorSeries().putAll(macdIndicators);

        getObserver().observe(CommandConstants.Services.macdService.name(), data.getSessionId(), data, MessageStatus.OKAY);

    }
}
