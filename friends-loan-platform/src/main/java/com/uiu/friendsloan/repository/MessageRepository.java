package com.uiu.friendsloan.repository;

import com.uiu.friendsloan.entity.Message;
import com.uiu.friendsloan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderAndReceiverOrReceiverAndSenderOrderBySentAtAsc(
            User sender, User receiver, User receiver2, User sender2);

    List<Message> findBySenderOrReceiverOrderBySentAtDesc(User user1, User user2);

    // User's all conversations (latest message with each user)
    @Query("SELECT m FROM Message m WHERE (m.sender = :user OR m.receiver = :user) " +
            "AND m.sentAt = (SELECT MAX(m2.sentAt) FROM Message m2 " +
            "WHERE (m2.sender = m.sender AND m2.receiver = m.receiver) " +
            "OR (m2.sender = m.receiver AND m2.receiver = m.sender))")
    List<Message> findLatestMessagesPerUser(@Param("user") User user);


    @Query("SELECT m FROM Message m " +
            "WHERE (m.sender.id = :currentUserId AND m.receiver.id = :otherUserId) " +
            "OR (m.sender.id = :otherUserId AND m.receiver.id = :currentUserId) " +
            "ORDER BY m.sentAt ASC")
    List<Message> findChatHistoryBetweenUsers(
            @Param("currentUserId") Long currentUserId,
            @Param("otherUserId") Long otherUserId);
}