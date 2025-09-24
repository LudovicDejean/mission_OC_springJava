
package com.chatop.controller;

import com.chatop.model.Message;
import com.chatop.repository.MessageRepository;
import com.chatop.repository.RentalRepository;
import com.chatop.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageRepository messageRepository;
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    public MessageController(MessageRepository messageRepository, RentalRepository rentalRepository, UserRepository userRepository){
        this.messageRepository = messageRepository;
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> send(@RequestBody Message m){
        m.setCreatedAt(LocalDateTime.now());
        if(m.getRental() != null && m.getRental().getId() != null){
            rentalRepository.findById(m.getRental().getId()).ifPresent(r -> m.setRental(r));
        }
        if(m.getUser() != null && m.getUser().getId() != null){
            userRepository.findById(m.getUser().getId()).ifPresent(u -> m.setUser(u));
        }
        Message saved = messageRepository.save(m);
        return ResponseEntity.ok(saved);
    }
}
