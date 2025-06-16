package com.example.service;

import com.example.entity.Message;
import com.example.exception.InvalidMessageException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Message createMessage(Message msg) {
        if (msg.getMessageText() == null || msg.getMessageText().trim().isEmpty()) {
            throw new InvalidMessageException("Message text is blank.");
        }
        if (msg.getMessageText().length() > 255) {
            throw new InvalidMessageException("Message text is too long.");
        }
        if (!accountRepository.existsById(msg.getPostedBy())) {
            throw new InvalidMessageException("User ID does not exist.");
        }

        return messageRepository.save(msg);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(int id) {
        return messageRepository.findById(id).orElse(null);
    }

    public List<Message> getMessagesByUser(int accountId) {
        return messageRepository.findByPostedBy(accountId);
    }

    public Integer deleteMessage(int id) {
        if (!messageRepository.existsById(id)) return 0;
        messageRepository.deleteById(id);
        return 1;
    }

    public int updateMessage(int id, Message updatedMsg) {
        Optional<Message> opt = messageRepository.findById(id);
        if (opt.isEmpty()) return 0;

        String newText = updatedMsg.getMessageText();
        if (newText == null || newText.trim().isEmpty() || newText.length() > 255) return 0;

        Message msg = opt.get();
        msg.setMessageText(newText);
        messageRepository.save(msg);
        return 1;
    }
}
