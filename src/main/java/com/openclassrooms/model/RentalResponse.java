package com.openclassrooms.model;


import lombok.Data;

@Data
public class RentalResponse {
	
	public String message;

	public RentalResponse(String message) {
		this.message = message;
	}

	public RentalResponse() {
	}

}
