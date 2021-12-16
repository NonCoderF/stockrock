package com.stockrock.analysis;

import com.stockrock.analysis.utils.SysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@Component
public class SocketConfig implements WebSocketConfigurer {

    private final SocketHandler socketHandler;

    @Autowired
    public SocketConfig(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        if (socketHandler == null) SysOut.e("Socket handler is null");
        assert socketHandler != null;
        registry.addHandler(socketHandler, "/term").setAllowedOrigins("*");
    }

}
