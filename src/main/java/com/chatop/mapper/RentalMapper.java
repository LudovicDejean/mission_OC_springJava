package com.chatop.mapper;

import com.chatop.dto.RentalDto;
import com.chatop.model.Rental;
import org.springframework.stereotype.Component;

@Component
public class RentalMapper {
	
	
	public RentalDto toDto(Rental entity, String url) {
		RentalDto dto = new RentalDto();
	    dto.setId(entity.getId());
	    dto.setName(entity.getName());
	    dto.setSurface(entity.getSurface());
	    dto.setPrice(entity.getPrice());
	    dto.setPicture(url+"/image?picture="+entity.getPicture());
	    dto.setDescription(entity.getDescription());
	    dto.setCreatedAt(entity.getCreatedAt());
	    dto.setUpdatedAt(entity.getUpdatedAt());
	    dto.setOwnerId(Math.toIntExact(entity.getOwner().getId()));
	    return dto;
	  }
	
	public RentalDto toDto(Rental entity) {
		RentalDto dto = new RentalDto();
	    dto.setId(entity.getId());
	    dto.setName(entity.getName());
	    dto.setSurface(entity.getSurface());
	    dto.setPrice(entity.getPrice());
	    dto.setPicture(entity.getPicture());
	    dto.setDescription(entity.getDescription());
	    dto.setCreatedAt(entity.getCreatedAt());
	    dto.setUpdatedAt(entity.getUpdatedAt());
	    dto.setOwnerId(Math.toIntExact(entity.getOwner().getId()));
	    return dto;
	  }
	  
	  public Rental toEntity(RentalDto dto) {
		Rental entity = new Rental();
		entity.setId(Long.valueOf(dto.getId()));
	    entity.setName(dto.getName());
	    entity.setSurface(dto.getSurface());
	    entity.setPrice(dto.getPrice());
	    entity.setPicture(dto.getPicture());
	    entity.setDescription(dto.getDescription());
	    entity.setCreatedAt(dto.getCreatedAt());
	    entity.setUpdatedAt(dto.getUpdatedAt());
	    return entity;
	  }

}
