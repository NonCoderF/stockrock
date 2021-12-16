package com.stockrock.analysis.service;

import com.stockrock.analysis.handlers.chart.OnChartServicesListener;
import com.stockrock.analysis.model.Data;
import com.stockrock.analysis.model.Stages;
import com.stockrock.analysis.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.stockrock.analysis.constants.CommandConstants.Services;

@Component
public class OhlcServices implements ServiceObserver {

    @Qualifier("ohlcService")
    final ServiceInterface ohlcService;
    @Qualifier("trendlineService")
    final ServiceInterface trendlineService;


    OnChartServicesListener l;
    Stages stages;

    @Autowired
    public OhlcServices(ServiceInterface ohlcService, ServiceInterface trendlineService) {
        this.ohlcService = ohlcService;
        this.trendlineService = trendlineService;
    }

    public void init(Stages stages, OnChartServicesListener listener) {
        this.l = listener;
        this.stages = stages;

        this.ohlcService.setStages(stages);
        this.trendlineService.setStages(stages);
        this.ohlcService.setObserver(this);
        this.trendlineService.setObserver(this);
    }

    public void initBarSeries(Data data) {

        DataUtils dataUtils = new DataUtils();
        if (dataUtils.isUpdatedData(data)) {

            l.onStarted(data.getSessionId(), Services.ohlcService.name());

            ohlcService.fetchBarsSeries(data);

            l.onEnded(data.getSessionId(), Services.ohlcService.name());

        }else{
            stages.addStages(1);
            ohlcService.setStages(stages);
            l.onCompleted(data.getSessionId(), Services.ohlcService.name(), data);
        }
    }

    @Override
    public void observe(String serviceName, String sessionId, Object data, String message) {
        if (serviceName.equals(Services.ohlcService.name()) && data == null){
            l.onFailed(sessionId, serviceName, message);
        }else if (serviceName.equals(Services.ohlcService.name())){
            Data dataObj = (Data) data;
            l.onCompleted(sessionId, serviceName, dataObj);

            l.onStarted(sessionId, Services.trendlineService.name());
            trendlineService.processTrendlines(dataObj);
            l.onEnded(sessionId, Services.ohlcService.name());

        }else if (serviceName.equals(Services.trendlineService.name())){
            Data dataObj = (Data) data;
            l.onCompleted(sessionId, serviceName, dataObj);
        }
    }
}
