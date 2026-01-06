package com.openclassrooms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message,Integer>{
	
}
