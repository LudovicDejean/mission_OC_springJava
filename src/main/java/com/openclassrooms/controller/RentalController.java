package com.openclassrooms.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.openclassrooms.dto.RentalDto;
import com.openclassrooms.model.DeleteResponse;
import com.openclassrooms.model.RentalResponse;
import com.openclassrooms.model.RentalsResponse;
import com.openclassrooms.model.User;
import com.openclassrooms.service.RentalService;
import com.openclassrooms.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
@Tag(name="Rental")
@SecurityRequirement(name="BearerAuth")
public class RentalController {

	private UserService userService;
	private RentalService rentalService;
	private final String imagePath;
	
	public RentalController(RentalService rentalService, UserService userService, @Value("${UPLOAD_DIR:uploads}")String imagePath) {
		this.imagePath = imagePath;
		this.rentalService = rentalService;
		this.userService = userService;
	}

	@Operation(summary = "Return all rentals", description = "This method returns all the rentals")
	@GetMapping("/rentals")
	public ResponseEntity<RentalsResponse> getRentals() {
		String url = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
		List<RentalDto> rentals = rentalService.getRentals(url);
		RentalsResponse rentalResponse = new RentalsResponse(rentals);
		return ResponseEntity.ok(rentalResponse);
	}

	@Operation(summary = "Find rental", description = "This method finds a rental by id")
	@GetMapping("/rentals/{id}")
	public ResponseEntity<RentalDto> getRental(@PathVariable int id) {
		String url = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
		RentalDto rental = rentalService.getRental(id,url.substring(0, url.length() - 3));
		return ResponseEntity.ok(rental);
	}

	@Operation(summary = "Create rental", description = "This method creates a new rental")
	@PostMapping(value = "/rentals/add", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<RentalResponse> addRental(
			@RequestParam(value="picture", required=false) MultipartFile file,
			@RequestParam("name") @NotBlank @Size(max = 63) String name, @RequestParam("surface") @Min(0) float surface,
			@RequestParam("price") @Min(0) float price,
			@RequestParam("description") @Size(max = 2000) String description, Authentication authentication) {
		String userName = authentication.getName();
		User user = userService.getUserByname(userName);
		RentalResponse rentalResponse = new RentalResponse();
		try {
			rentalService.saveRental(name, surface, price, description, user, file);
			rentalResponse.message = "Rental created !";
			return new ResponseEntity<>(rentalResponse, HttpStatus.CREATED);
		} catch (Exception e) {
			rentalResponse.message = "Something went wrong !";
			return new ResponseEntity<>(rentalResponse, HttpStatus.BAD_REQUEST);
		}
	}	
	@GetMapping(value="/rentals/image",
			produces= {MediaType.IMAGE_JPEG_VALUE,
					MediaType.IMAGE_PNG_VALUE,
					MediaType.IMAGE_GIF_VALUE})
	public ResponseEntity<byte[]> getImage(@RequestParam("picture") String image){
		try {
			String filePath = imagePath+image;
			 byte[] bytes = Files.readAllBytes(Paths.get(filePath));
			return new ResponseEntity<>(bytes, HttpStatus.OK);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();			
		}
	}
	@DeleteMapping("rentals/delete/{id}")
	public ResponseEntity<DeleteResponse> deleteRental(@PathVariable int id) {
		rentalService.deleteRental(id);
		DeleteResponse deleteResponse = new DeleteResponse("Rental deleted !");
		return new ResponseEntity<>(deleteResponse,HttpStatus.NO_CONTENT);
	}
	@Operation(summary = "Update rental", description = "This method updates a rental")
	@PutMapping("/rentals/update/{id}")
	public ResponseEntity<RentalResponse> updateRental(@RequestParam("name") @NotBlank @Size(max = 63) String name,
			@RequestParam("surface") @Min(0) float surface, @RequestParam("price") @Min(0) float price,
			@RequestParam("description") @Size(max = 2000) String description, @PathVariable int id)
			throws IOException {
		rentalService.updateRental(name, surface, price, description, id);
		RentalResponse rentalResponse = new RentalResponse("Rental updated!");
		return new ResponseEntity<>(rentalResponse, HttpStatus.OK);
	}
}