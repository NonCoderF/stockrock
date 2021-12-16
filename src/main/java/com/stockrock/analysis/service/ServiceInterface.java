package com.stockrock.analysis.service;

import com.stockrock.analysis.model.Data;
import com.stockrock.analysis.model.Stages;
import org.ta4j.core.BarSeries;

public abstract class ServiceInterface {

    private Stages stages;
    private ServiceObserver observer;

    public void setObserver(ServiceObserver observer){
        this.observer = observer;
    };

    public ServiceObserver getObserver() {
        return observer;
    }

    public void processAdxIndicators(Data data, int[] adxCounts, BarSeries series){
        addStages();
    }

    public void processClosePriceIndicator(Data data, BarSeries series){
        addStages();
    }

    public void processSmaIndicator(Data data, int[] barCounts, BarSeries series){addStages();}

    public void processEmaIndicator(Data data, int[] barCounts, BarSeries series){addStages();}

    public void processRSIIndicator(Data data, int[] barCounts, BarSeries series){addStages();}

    public void processOBVIndicator(Data data, BarSeries series){addStages();}

    public void processMACDIndicator(Data data, int[] barCounts, BarSeries series){addStages();}

    public void processTrendlines(Data data){addStages();}

    public void fetchBarsSeries(Data data) { addStages(); }

    public void setStages(Stages stages) {
        this.stages = stages;
    }

    private synchronized void addStages(){
        if (stages != null)
            stages.addStages(1);
    }

    public void executeTerminalCommand(String command){};

}
