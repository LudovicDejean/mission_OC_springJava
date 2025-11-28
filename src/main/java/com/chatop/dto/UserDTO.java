package com.chatop.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
		
	private Long id;
	private String email;
	private String name;
	@JsonProperty("created_at")
	private LocalDateTime createdAt ;
	@JsonProperty("updated_at")
	private LocalDateTime updatedAt ;


}
