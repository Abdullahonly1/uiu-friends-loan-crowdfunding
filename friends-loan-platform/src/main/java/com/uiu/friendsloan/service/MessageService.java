package com.uiu.friendsloan.service;

import com.uiu.friendsloan.entity.Message;
import com.uiu.friendsloan.entity.User;
import com.uiu.friendsloan.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;

    public MessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    @Transactional
    public Message sendMessage(User receiver, String content, Long loanPostId, Long crowdfundingPostId) {
        User sender = userService.getCurrentUser();

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .sentAt(LocalDateTime.now())
                .loanPost(null)           // পরে লিঙ্ক করতে পারবে
                .crowdfundingPost(null)
                .build();

        Message saved = messageRepository.save(message);

        System.out.println("✅ Message Sent: " + sender.getEmail() + " → " + receiver.getEmail() + " | Content: " + content);
        return saved;
    }

    public List<Message> getChatHistory(Long otherUserId) {
        Long currentUserId = userService.getCurrentUser().getId();
        return messageRepository.findChatHistoryBetweenUsers(currentUserId, otherUserId);
    }

    // নতুন: User's all conversations
    /**
     * Logged in user এর সব conversations (latest message সহ)
     */
    public List<Map<String, Object>> getMyConversations() {
        User currentUser = userService.getCurrentUser();

        List<Message> latestMessages = messageRepository.findLatestMessagesPerUser(currentUser);

        return latestMessages.stream().map(msg -> {
            User otherUser = msg.getSender().getId().equals(currentUser.getId())
                    ? msg.getReceiver() : msg.getSender();

            Map<String, Object> map = new HashMap<>();
            map.put("userId", otherUser.getId());
            map.put("name", otherUser.getName());
            map.put("lastMessage", msg.getContent());
            map.put("sentAt", msg.getSentAt());
            return map;
        }).collect(Collectors.toList());
    }

    // Real-time এর জন্য
    public Message sendRealTimeMessage(User receiver, String content, Long loanPostId, Long cfPostId) {
        User sender = userService.getCurrentUser();

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .loanPost(loanPostId != null ? /* load post */ null : null)
                .crowdfundingPost(cfPostId != null ? /* load post */ null : null)
                .sentAt(LocalDateTime.now())
                .build();

        Message saved = messageRepository.save(message);

        // Real-time পাঠানোর জন্য (পরে WebSocket দিয়ে করবো)
        System.out.println("📨 Real-time Message: " + sender.getName() + " → " + receiver.getName());
        return saved;
    }
}