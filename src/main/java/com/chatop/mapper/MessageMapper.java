package com.chatop.mapper;

import com.chatop.dto.MessageDTO;
import com.chatop.model.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {
	
	public MessageDTO toDto(Message entity) {
		MessageDTO dto = new MessageDTO();
		dto.setMessage(entity.getMessage());
		return dto;
	}
	public Message toEntity(MessageDTO dto) {
		Message entity = new Message();
		entity.setMessage(dto.getMessage());
		return entity;
	}

}
