package com.stockrock.analysis;

import com.stockrock.analysis.handlers.EndPointHandler;
import com.stockrock.analysis.utils.Messenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import static com.stockrock.analysis.constants.MessageStatus.CONNECTED;
import static com.stockrock.analysis.constants.MessageStatus.DISCONNECTED;

@Component
public class SocketHandler extends TextWebSocketHandler {

    private final EndPointHandler endPointHandler;

    @Autowired
    public SocketHandler(EndPointHandler endPointHandler) {
        this.endPointHandler = endPointHandler;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        endPointHandler.handleSessionMessage(session, message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Messenger.sendOkMessage(session, CONNECTED);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Messenger.sendOkMessage(session, DISCONNECTED);
        super.afterConnectionClosed(session, status);
    }
}
