package com.stockrock.analysis.handlers;

import com.stockrock.analysis.model.Message;

public interface ServiceExecuteListener {

    void onDeliver(Message message);

}
