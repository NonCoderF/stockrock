package com.stockrock.analysis.handlers.chart;

import com.stockrock.analysis.model.Data;
import com.stockrock.analysis.model.indicators.Indicator;

import java.util.HashMap;

import static com.stockrock.analysis.constants.CommandConstants.Indicators.*;

public class IndicatorCmdHandler {

    private final Data data;

    public IndicatorCmdHandler(Data data) {
        this.data = data;

        flushIndicators();
    }

    public Indicator closePrice(){
        return getIndicator(closePrice.name());
    }

    public Indicator adx(){
        return getIndicator(adx.name());
    }

    public Indicator sma(){
        return getIndicator(sma.name());
    }

    public Indicator ema(){
        return getIndicator(ema.name());
    }

    public Indicator rsi(){
        return getIndicator(rsi.name());
    }

    public Indicator obv(){
        return getIndicator(obv.name());
    }

    public Indicator macd(){
        return getIndicator(macd.name());
    }

    private Indicator getIndicator(String indicatorName){
        Indicator indicator = null;
        for (int i = 0; i < data.getIndicators().size(); i++){
            if (data.getIndicators().get(i).getName().contains(indicatorName)){
                indicator = data.getIndicators().get(i);
                break;
            }
        }
        return indicator;
    }

    private void flushIndicators(){
        this.data.setIndicatorSeries(new HashMap<>());
    }
}
