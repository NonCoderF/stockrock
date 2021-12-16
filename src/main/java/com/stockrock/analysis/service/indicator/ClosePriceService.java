package com.stockrock.analysis.service.indicator;

import com.stockrock.analysis.constants.CommandConstants;
import com.stockrock.analysis.constants.MessageStatus;
import com.stockrock.analysis.model.Data;
import com.stockrock.analysis.service.ServiceInterface;
import org.springframework.stereotype.Service;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.stockrock.analysis.constants.CommandConstants.Services.closePriceService;

@Service
public class ClosePriceService extends ServiceInterface {

    @Override
    public void processClosePriceIndicator(Data data, BarSeries series) {
        super.processClosePriceIndicator(data, series);
        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(series);

        HashMap<String, List<Double>> closePrice = new HashMap<>();

        List<Double> closeIndicator = new ArrayList<>();
        for (int i = 0; i < series.getBarCount(); i++){
            closeIndicator.add(closePriceIndicator.getValue(i).doubleValue());
        }

        closePrice.put(CommandConstants.Indicators.closePrice.name(), closeIndicator);

        data.getIndicatorSeries().putAll(closePrice);

        getObserver().observe(closePriceService.name(), data.getSessionId(), data, MessageStatus.OKAY);
    }
}
