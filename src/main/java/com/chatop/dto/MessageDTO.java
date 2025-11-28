package com.chatop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
	
	@JsonProperty("rental_id")
	private Integer rentalId;
	@JsonProperty("user_id")
	private Integer userId;
	private String message;

}
