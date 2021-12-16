package com.stockrock.analysis;

import com.stockrock.analysis.model.Command;
import com.stockrock.analysis.model.Data;
import com.stockrock.analysis.model.Stages;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

public class GlobalData {

    public static Map<String, Data> dataMap = new HashMap<>();
    public static Map<String, WebSocketSession> sessionMap = new HashMap<>();
    public static Map<String, Stages> stagesMap = new HashMap<>();

    public static TextMessage message = null;
    public static Command command = null;

}
