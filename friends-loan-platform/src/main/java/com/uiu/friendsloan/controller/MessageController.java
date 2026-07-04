package com.uiu.friendsloan.controller;

import com.uiu.friendsloan.entity.Message;
import com.uiu.friendsloan.entity.User;
import com.uiu.friendsloan.service.MessageService;
import com.uiu.friendsloan.service.UserService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(MessageService messageService,
                             UserService userService,
                             SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    // REST API - Send Message
    @PostMapping
    public Message sendMessage(@RequestBody Map<String, Object> request) {
        Long receiverId = Long.valueOf(request.get("receiverId").toString());
        String content = request.get("content").toString();

        User receiver = userService.getUserById(receiverId);
        Message saved = messageService.sendMessage(receiver, content, null, null);

        // Real-time
        messagingTemplate.convertAndSendToUser(receiver.getEmail(), "/queue/messages", saved);

        return saved;
    }

    // Get Chat History
    @GetMapping("/history/{receiverId}")
    public List<Message> getHistory(@PathVariable Long receiverId) {
        return messageService.getChatHistory(receiverId);
    }

    // নতুন Endpoint (Conversations List)
    @GetMapping("/conversations")
    public List<Map<String, Object>> getMyConversations() {
        return messageService.getMyConversations();   // তোমার service এ এই মেথড বানাতে হবে
    }


}