package com.stockrock.analysis.service.configs;

import com.stockrock.analysis.utils.SysOut;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
        throwable.printStackTrace();
        SysOut.e("Exception Cause - " + throwable.getMessage());
        SysOut.e("Method name - " + method.getName());
        for (Object param : obj) {
            SysOut.e("Parameter value - " + param);
        }
    }
}
