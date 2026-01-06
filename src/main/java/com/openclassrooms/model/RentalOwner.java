package com.openclassrooms.model;

import lombok.Data;

@Data
public class RentalOwner {
	
	private String name;
	private Float surface;
	private Float price;
	private String picture;
	private String description;
	public RentalOwner() {
	}
	public RentalOwner(String name, Float surface, Float price, String description, String picture) {
		this.name = name;
		this.surface = surface;
		this.price = price;
		this.picture = picture;
		this.description = description;
	}
	
}
