package com.github.rishabh9.rikostarter.services;

import com.github.rishabh9.riko.upstox.websockets.WebSocketService;
import com.github.rishabh9.riko.upstox.websockets.models.WrappedWebSocket;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

@Log4j2
@Service
public class UpstoxWebSocketService {

    private static WrappedWebSocket webSocket;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private UpstoxWebSocketSubscriber upstoxWebSocketSubscriber;

    public void connect() {
        log.info("Connecting to web-socket...");
        try {
            webSocket = webSocketService
                    .connect(Collections.singletonList(upstoxWebSocketSubscriber));
        } catch (ExecutionException | InterruptedException e) {
            log.error("Unable to make the web-socket connection", e);
        }
    }

    public void disconnect() {
        log.info("Disconnecting from web-socket...");
        cleanSubscriber();
        closeWebSocket();
    }

    private void closeWebSocket() {
        if (null != webSocket) {
            log.info("Disconnecting from web-socket");
            webSocket.cancel();
            webSocket.close();
            webSocket = null;
            log.info("Disconnected from web-socket");
        }
    }

    private void cleanSubscriber() {
        if (null != upstoxWebSocketSubscriber) {
            log.warn("Stopping WebSocket subscriber");
            upstoxWebSocketSubscriber.cleanUp();
        }
    }
}

