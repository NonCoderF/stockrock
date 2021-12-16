package com.stockrock.analysis.utils;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class Messenger {

    public static void sendErrorMessage(WebSocketSession session, String message){
        sendSocketMessage(session, new TextMessage("{ \"id\" : \"" + session.getId() + "\", \"status\" : \"ERROR\", \"message\" : \"" + message + "\" }"));
    }

    public static void sendOkMessage(WebSocketSession session, String message){
        sendSocketMessage(session, new TextMessage("{ \"id\" : \"" + session.getId() + "\", \"status\" : \"OK\", \"message\" : \"" + message + "\" }"));
    }

    public static void sendOkMessageObj(WebSocketSession session, String object){
        sendSocketMessage(session, new TextMessage("{ \"id\" : \"" + session.getId() + "\", \"status\" : \"OK\", \"message\" : \"Success\" , \"data\" : " + object + " }"));
    }

    public static void sendErrorMessageObj(WebSocketSession session, String object){
        sendSocketMessage(session, new TextMessage("{ \"id\" : \"" + session.getId() + "\", \"status\" : \"ERROR\", \"message\" : \"Failed\" , \"data\" : " + object + " }"));
    }

    private static void sendSocketMessage(WebSocketSession session, TextMessage errorMessage) {
        try {
            session.sendMessage(errorMessage);
        } catch (IOException e) {
            SysOut.e(e.getMessage());
        }
    }

    public static void sendDataMessage(WebSocketSession session, String data){
        sendSocketMessage(session, new TextMessage("{ \"id\" : \"" + session.getId() + "\", \"data\" : " + data + " }"));
    }


}
