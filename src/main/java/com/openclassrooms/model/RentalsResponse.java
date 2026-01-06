package com.openclassrooms.model;

import java.util.List;

import com.openclassrooms.dto.RentalDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RentalsResponse {
	
	private List<RentalDto> rentals;

}
