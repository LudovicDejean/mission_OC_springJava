package com.chatop.mapper;

import com.chatop.dto.UserDTO;
import com.chatop.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
	
	public UserDTO toDto(User entity) {
		UserDTO dto = new UserDTO();
	    dto.setId(entity.getId());
	    dto.setName(entity.getName());
	    dto.setEmail(entity.getEmail());
	    dto.setCreatedAt(entity.getCreatedAt());
	    dto.setUpdatedAt(entity.getUpdatedAt());

	    return dto;
	  }

	  public User toEntity(UserDTO dto) {
		  User entity = new User();
	    entity.setId(dto.getId());
	    entity.setName(dto.getName());
	    entity.setEmail(dto.getEmail());
	    entity.setCreatedAt(dto.getCreatedAt());
	    entity.setUpdatedAt(dto.getUpdatedAt());
	    
	    return entity;
	  }

}
