package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.ChatDTO;
import ro.tuc.ds2020.services.ChatService;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/message")
    @SendTo("/public/public")
    public ChatDTO receiveMessage(@Payload ChatDTO content){
        return content;
    }

    @MessageMapping("/private-message")
    public ChatDTO receivePrivateMessage(@Payload ChatDTO message){
        simpMessagingTemplate.convertAndSendToUser(message.getReceptor(),"/private", chatService.insert(message));
        return message;
    }

    @PutMapping("/showmessage")
    public ResponseEntity<?> clientsMessages(@RequestBody ChatDTO chatDTO)
    {
        return new ResponseEntity<>(chatService.getChat(chatDTO), HttpStatus.OK);
    }
}
