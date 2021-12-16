package com.stockrock.analysis.service.indicator;

import com.stockrock.analysis.constants.CommandConstants.Indicators;
import com.stockrock.analysis.constants.MessageStatus;
import com.stockrock.analysis.model.Data;
import com.stockrock.analysis.model.indicators.Adx;
import com.stockrock.analysis.service.ServiceInterface;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.adx.ADXIndicator;
import org.ta4j.core.indicators.adx.MinusDIIndicator;
import org.ta4j.core.indicators.adx.PlusDIIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.stockrock.analysis.constants.CommandConstants.Services.adxService;
import static com.stockrock.analysis.service.configs.ExecutorConfig.THREAD_POOL;

@Service
public class AdxService extends ServiceInterface {

    @Override
    @Async(THREAD_POOL)
    public void processAdxIndicators(Data data, int[] adxCounts, BarSeries series) {
        super.processAdxIndicators(data, adxCounts, series);

        HashMap<String, List<Double>> adxMap = new HashMap<>();

        for (int adxCount : adxCounts) {
            final ADXIndicator adxIndicator = new ADXIndicator(series, adxCount);
            final PlusDIIndicator plusDIIndicator = new PlusDIIndicator(series, adxCount);
            final MinusDIIndicator minusDIIndicator = new MinusDIIndicator(series, adxCount);

            Adx adx = new Adx();
            adx.setAdxCount(adxCount);
            List<Double> adxValues = new ArrayList<>();
            List<Double> plusDiValues = new ArrayList<>();
            List<Double> minusDiValues = new ArrayList<>();

            for (int i = 0; i < series.getBarCount(); i++){
                adxValues.add(adxIndicator.getValue(i).doubleValue());
                plusDiValues.add(plusDIIndicator.getValue(i).doubleValue());
                minusDiValues.add(minusDIIndicator.getValue(i).doubleValue());
            }

            adx.setAdx(adxValues);
            adx.setPlusDi(plusDiValues);
            adx.setMinusDi(minusDiValues);

            adxMap.put(Indicators.adx.name() + "_" + adxCount, adx.getAdx());
            adxMap.put(Indicators.adx.name() + "PlusDi_" + adxCount, adx.getPlusDi());
            adxMap.put(Indicators.adx.name() + "MinusDi_" + adxCount, adx.getMinusDi());
        }

        data.getIndicatorSeries().putAll(adxMap);

        if (getObserver() != null){
            getObserver().observe(adxService.name(), data.getSessionId(), data, MessageStatus.OKAY);
        }
    }
}
