package com.stockrock.analysis.handlers.chart;

import com.stockrock.analysis.model.Data;

public interface OnChartServicesListener {

    void onStarted(String sessionId, String serviceName);

    void onCompleted(String sessionId, String serviceName, Data data);

    void onEnded(String sessionId, String serviceName);

    void onFailed(String sessionId, String serviceName, String failMessage);

}
