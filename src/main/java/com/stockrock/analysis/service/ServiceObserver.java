package com.stockrock.analysis.service;

public interface ServiceObserver {

    void observe(String serviceName, String sessionId, Object data, String message);
}
