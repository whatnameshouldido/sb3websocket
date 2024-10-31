package com.kjs.sb3websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            ChatMessageDto chatMessageDto = objectMapper.readValue(payload, ChatMessageDto.class);
            ChatRoomDto byRoomId = this.chatRoomService.findByRoomId(chatMessageDto.getRoomId());
            List<WebSocketSession> sessionList = byRoomId.getSessionList();
            if (chatMessageDto.getMsgType() == ChatMessageDto.ChatMessageType.ENTER) {
                sessionList.add(session);
                this.sendMessageSessionsInRoom(chatMessageDto, sessionList);
            } else if (chatMessageDto.getMsgType() == ChatMessageDto.ChatMessageType.OUT) {
                sessionList.remove(session);
                this.sendMessageSessionsInRoom(chatMessageDto, sessionList);
            } else if (chatMessageDto.getMsgType() == ChatMessageDto.ChatMessageType.MESSAGE) {
                this.sendMessageSessionsInRoom(chatMessageDto, sessionList);
            }
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    private void sendMessageSessionsInRoom(ChatMessageDto chatMessageDto, List<WebSocketSession> sessionList) throws IOException {
        String msg = this.objectMapper.writeValueAsString(chatMessageDto.getMessage());
        TextMessage tm = new TextMessage(msg);
        for (WebSocketSession webSocketSession : sessionList) {
            try {
                webSocketSession.sendMessage(tm);
            } catch (IOException e) {
                log.error(e.toString());
            }
        }
    }
}