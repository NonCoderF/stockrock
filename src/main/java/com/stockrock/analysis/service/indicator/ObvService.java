package com.stockrock.analysis.service.indicator;

import com.stockrock.analysis.constants.CommandConstants;
import com.stockrock.analysis.constants.CommandConstants.Indicators;
import com.stockrock.analysis.constants.MessageStatus;
import com.stockrock.analysis.model.Data;
import com.stockrock.analysis.service.ServiceInterface;
import org.springframework.stereotype.Service;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.volume.OnBalanceVolumeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ObvService extends ServiceInterface {

    @Override
    public void processOBVIndicator(Data data, BarSeries series) {
        super.processOBVIndicator(data, series);

        HashMap<String, List<Double>> obvIndicators = new HashMap<>();


        OnBalanceVolumeIndicator shortObv = new OnBalanceVolumeIndicator(series);

        List<Double> obv = new ArrayList<>();

        for (int i = 0; i < series.getBarCount(); i++) {
            obv.add(shortObv.getValue(i).doubleValue());
        }
        obvIndicators.put(Indicators.obv.name(), obv);

        data.getIndicatorSeries().putAll(obvIndicators);

        getObserver().observe(CommandConstants.Services.obvService.name(), data.getSessionId(), data, MessageStatus.OKAY);

    }
}
