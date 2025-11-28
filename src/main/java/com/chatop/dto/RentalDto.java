package com.chatop.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalDto {
	
	private Long id;
	private String name;
	private Double surface;
	private Double price;
	private String picture;
	private String description;
	@JsonProperty("owner_id")
	private Integer ownerId;
	@JsonProperty("created_at")
	private LocalDateTime createdAt ;
	@JsonProperty("updated_at")
	private LocalDateTime updatedAt;

}
