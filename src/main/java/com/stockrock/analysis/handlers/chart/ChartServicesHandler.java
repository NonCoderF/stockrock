package com.stockrock.analysis.handlers.chart;

import com.stockrock.analysis.GlobalData;
import com.stockrock.analysis.constants.CommandConstants;
import com.stockrock.analysis.constants.MessageStatus;
import com.stockrock.analysis.handlers.ServiceExecuteListener;
import com.stockrock.analysis.model.Data;
import com.stockrock.analysis.model.Message;
import com.stockrock.analysis.model.Stages;
import com.stockrock.analysis.service.IndicatorServices;
import com.stockrock.analysis.service.OhlcServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.stockrock.analysis.constants.CommandConstants.chart;

@Component
public class ChartServicesHandler implements OnChartServicesListener {

    final OhlcServices ohlcServices;
    final IndicatorServices indicatorServices;

    private ServiceExecuteListener listener;

    @Autowired
    public ChartServicesHandler(OhlcServices ohlcServices, IndicatorServices indicatorServices) {
        this.ohlcServices = ohlcServices;
        this.indicatorServices = indicatorServices;
    }

    public void setListener(ServiceExecuteListener listener) {
        this.listener = listener;
    }

    public void init(Data data) {
        ohlcServices.init(getStages(data.getSessionId()), this);
        ohlcServices.initBarSeries(data);
    }

    public void onBarSeriesReady(Data data) {
        indicatorServices.init(getStages(data.getSessionId()), this);
        indicatorServices.initIndicators(data);
    }

    @Override
    public void onStarted(String sessionId, String serviceName) {
        deliverMessage(sessionId, serviceName, MessageStatus.INITIATING, "", null);
    }

    @Override
    public void onCompleted(String sessionId, String serviceName, Data data) {
        if (serviceName.equalsIgnoreCase(CommandConstants.Services.ohlcService.name())) {
            saveData(data);
            onBarSeriesReady(data);
        }
        getStages(sessionId).incCurrentStage();
        deliverMessage(sessionId, serviceName, MessageStatus.COMPLETED, "", data);
    }

    @Override
    public void onEnded(String sessionId, String serviceName) {
        deliverMessage(sessionId, serviceName, MessageStatus.INITIATED, "", null);
    }

    @Override
    public void onFailed(String sessionId, String serviceName, String message) {
        getStages(sessionId).incCurrentStage();
        deliverMessage(sessionId, serviceName, MessageStatus.FAILED, message, null);
    }

    private void deliverMessage(String sessionId, String serviceName, String status, String info, Data data) {
        Message message = new Message();
        message.setType(chart);
        message.setSessionId(sessionId);
        message.setCurrentStage(getStages(sessionId).getCurrentStage());
        message.setStages(getStages(sessionId).getTotalStages());
        message.setProgress(getStages(sessionId).getProgress());
        message.setStatus(status);
        message.setInfo(info);
        message.setService(serviceName);

        if (data != null) {
            message.setId(data.getId());
            message.setData(data);
            message.setLive(data.getRealtime());
        }

        listener.onDeliver(message);
    }

    private Stages getStages(String sessionId) {
        return GlobalData.stagesMap.get(sessionId);
    }

    private void saveData(Data data) {
        GlobalData.dataMap.put(data.getId(), data);
    }
}
