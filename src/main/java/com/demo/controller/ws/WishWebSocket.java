package com.demo.controller.ws;

import com.demo.entity.Wish;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Spring WebSocket 广播新愿望
 */


@Component
@ServerEndpoint("/ws/wishes")
public class WishWebSocket {

    private static final CopyOnWriteArraySet<Session> sessions = new CopyOnWriteArraySet<>();
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WishWebSocket.class);

    @OnOpen
    public void onOpen(Session session) {
        log.info("WebSocket onOpen, id={}", session.getId());

        // 打印当前线程信息
        Thread t = Thread.currentThread();
        log.info("""
                           🔌 WebSocket onOpen 线程: {}
                           虚拟线程? {}
                        """,
                t.getName(),
                t.isVirtual());


        sessions.add(session);
        log.info("sessions.size={}", sessions.size());

    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    public static void broadcast(Wish wish) {

        Thread t = Thread.currentThread();
        log.info("📣 broadcast 调用线程:{} | 虚拟线程?{}   ", t.getName()
                , t.isVirtual());

        String json;
        try {
            json = objectMapper.writeValueAsString(wish);
        } catch (Exception e) {
            log.error("broadcast write error:{}", e.getMessage());
            return;
        }
        sessions.forEach(session -> {
            try {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(json);
                    log.info("sessions sent successfully:{}", json);
                }
            } catch (IOException e) {
                log.error("sessions sent error:{}", e.getMessage());
                sessions.remove(session);
            }
        });
    }
}
