package com.kjs.sb3websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/chat")
public class ChatRoomController {
    @Autowired
    private ChatRoomService chatRoomService;

    @GetMapping("/list")
    public String chatRoomList(Model model) {
        model.addAttribute("chatRoomList", this.chatRoomService.findAll());
        return "chat/chatroomlist";
    }
    @PostMapping("/create")
    public String createChatRoom(Model model, String roomName) {
        this.chatRoomService.insert(roomName);
        return "redirect:/chat/list";
    }

    @GetMapping("/enter")   // GET ? 와 &
    // @GetMapping("/enter/{roomId}/{writer}") // GET 주소로 데이터 전달
    public String enterChatRoom(Model model
            , @RequestParam String roomId   // GET ? 와 &
//            , @PathVariable String roomId   // GET 주소
            , @RequestParam String writer   // GET ? 와 &
//            , @PathVariable String writer   // GET 주소
    ) {
        model.addAttribute("chatRoomDto", this.chatRoomService.findByRoomId(roomId));
        model.addAttribute("writer", writer);
        return "chat/chatroomdetail";
    }
}
