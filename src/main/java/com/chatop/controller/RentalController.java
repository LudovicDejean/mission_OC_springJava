
package com.chatop.controller;

import com.chatop.model.Rental;
import com.chatop.repository.UserRepository;
import com.chatop.service.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private final RentalService service;
    private final UserRepository userRepository;
    private final String uploadDir;

    public RentalController(RentalService service, UserRepository userRepository, org.springframework.core.env.Environment env){
        this.service = service; this.userRepository = userRepository;
        this.uploadDir = env.getProperty("chatop.upload.dir","uploads");
        File dir = new File(this.uploadDir);
        if(!dir.exists()) dir.mkdirs();
    }

    @GetMapping
    public List<Rental> all(){ return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Rental> get(@PathVariable Long id){
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<?> create(@RequestPart("rental") Rental r, @RequestPart(value="file", required=false) MultipartFile file) throws IOException {
        if(file != null && !file.isEmpty()){
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path target = Paths.get(uploadDir).resolve(filename);
            Files.copy(file.getInputStream(), target);
            r.setPicture("/" + uploadDir + "/" + filename);
        }
        if(r.getOwner() != null && r.getOwner().getId() != null){
            userRepository.findById(r.getOwner().getId()).ifPresent(r::setOwner);
        }
        Rental saved = service.save(r);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<?> delete(@PathVariable Long id){
        service.delete(id); return ResponseEntity.noContent().build();
    }
}
