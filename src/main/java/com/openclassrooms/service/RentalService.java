package com.openclassrooms.service;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.openclassrooms.dto.RentalDto;
import com.openclassrooms.mapper.RentalMapper;
import com.openclassrooms.model.Rental;
import com.openclassrooms.model.RentalOwner;
import com.openclassrooms.model.User;
import com.openclassrooms.repository.RentalRepository;

@Service
public class RentalService {
	
	private final RentalMapper rentalMapper;
	private String imagePath;
	private final RentalRepository rentalRepository;

	public RentalService(RentalRepository rentalRepository, RentalMapper rentalMapper, @Value("${UPLOAD_DIR:uploads}") String imagePath) {
		this.imagePath = imagePath;
		this.rentalRepository = rentalRepository;
		this.rentalMapper = rentalMapper;
	}

	public RentalDto getRental(final int id, String url) {
		Rental rental = rentalRepository.findById(id).orElse(null);
		return rentalMapper.toDto(rental,url);
	}

	public List<RentalDto> getRentals(String url) {
		List<Rental> list = rentalRepository.findAll();
		List<RentalDto> dto = new ArrayList<>();
		list.forEach(rental -> { 
			dto.add(rentalMapper.toDto(rental,url));
			});
		return dto;
	}

	public void deleteRental(final int id) {
		rentalRepository.deleteById(id);
	}

	public void saveRental(String name, float surface, float price, String description, User user, MultipartFile file)
			throws IllegalStateException, IOException {
		
		String fileName =StringUtils.cleanPath(file.getOriginalFilename());
		String picture = fileName;
		saveFile(fileName, file);
		RentalOwner rentalOwner = new RentalOwner(name, surface, price, description, picture);
		Rental rental = rentalMapper.RantalOwnertoEntity(rentalOwner);
		rental.setOwner(user);
		rentalRepository.save(rental);		
		}

	public void updateRental(String name, float surface, float price, String description, final int id) {
		Rental rental = rentalRepository.findById(id).orElse(null);
		LocalDateTime now = LocalDateTime.now();
		rental.setName(name);
		rental.setSurface(surface);
		rental.setPrice(price);
		rental.setDescription(description);
		rental.setUpdatedAt(now);
		rentalRepository.save(rental);
	}
	public void saveFile(String fileName, MultipartFile multipartFile)
            throws IOException {
		Path uploadPath = Paths.get(imagePath.substring(0, imagePath.length() - 1));
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }     
		try (InputStream inputStream = multipartFile.getInputStream()) {
	            Path filePath = uploadPath.resolve(fileName);
	            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
	        } catch (IOException ioe) {       
	            throw new IOException("Could not save file: " + fileName, ioe);
	        }
    }  
        
}