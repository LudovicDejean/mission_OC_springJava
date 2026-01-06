package com.openclassrooms.mapper;

import org.springframework.stereotype.Component;

import com.openclassrooms.dto.MessageDTO;
import com.openclassrooms.model.Message;

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
