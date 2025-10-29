
package com.chatop.service;

import com.chatop.model.Rental;
import com.chatop.repository.RentalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RentalService {
    private final RentalRepository repo;
    public RentalService(RentalRepository repo){this.repo = repo;}
    public List<Rental> findAll(){return repo.findAll();}
    public Optional<Rental> findById(Long id){return repo.findById(id);}
    public Rental save(Rental r){return repo.save(r);}
    public void delete(Long id){repo.deleteById(id);}
}
