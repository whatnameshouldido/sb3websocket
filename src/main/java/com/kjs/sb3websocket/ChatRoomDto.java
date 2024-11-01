package com.kjs.sb3websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {
    private String roomName;
    private String roomId;
    private final List<ChatWebSocketSession> chatWebSocketSessions = new ArrayList<>();

    public Integer getCount() {
        return chatWebSocketSessions.size();
    }
}
