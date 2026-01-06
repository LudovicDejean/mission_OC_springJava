package com.openclassrooms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MessageDTO {
	
	@JsonProperty("rental_id")
	private Long rentalId;
	@JsonProperty("user_id")
	private Long userId;
	private String message;

}
