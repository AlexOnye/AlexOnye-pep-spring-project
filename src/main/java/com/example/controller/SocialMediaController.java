package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.example.exception.InvalidAccountException;
import com.example.exception.InvalidMessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account acc) {
        try {
            Account registered = accountService.register(acc);
            return ResponseEntity.ok(registered);
        } catch (InvalidAccountException e) {
            if (e.getMessage().toLowerCase().contains("exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
            }
            return ResponseEntity.badRequest().build(); // 400
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account acc) {
        try {
            return ResponseEntity.ok(accountService.login(acc));
        } catch (InvalidAccountException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message msg) {
        try {
            return ResponseEntity.ok(messageService.createMessage(msg));
        } catch (InvalidMessageException e) {
            return ResponseEntity.badRequest().build(); // 400
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable int id) {
        Message msg = messageService.getMessageById(id);
        return (msg != null) ? ResponseEntity.ok(msg) : ResponseEntity.ok().build();
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable int id) {
        Integer deleted = messageService.deleteMessage(id);
        return deleted == 1 ? ResponseEntity.ok(1) : ResponseEntity.ok().build();
    }

    @PatchMapping("/messages/{id}")
    public ResponseEntity<Integer> updateMessage(@PathVariable int id, @RequestBody Message updatedMsg) {
        int updated = messageService.updateMessage(id, updatedMsg);
        return updated == 1 ? ResponseEntity.ok(1) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable int accountId) {
        return ResponseEntity.ok(messageService.getMessagesByUser(accountId));
    }
}
