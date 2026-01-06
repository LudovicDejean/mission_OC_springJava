package com.openclassrooms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.dto.MessageDTO;
import com.openclassrooms.model.MessageResponse;
import com.openclassrooms.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
@Tag(name="Message")
@SecurityRequirement(name="BearerAuth")
public class MessageController {

	private MessageService messageService;

	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	@Operation(description = "Send a message", summary = "This method allows user to send a message to the owner")
	@PostMapping("/messages")
	public ResponseEntity<MessageResponse> addMessage(
			@RequestBody MessageDTO messageDto) {
		messageService.saveMessage(messageDto);
		MessageResponse messageResponse = new MessageResponse("Message sent!");
		return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
	}

}
