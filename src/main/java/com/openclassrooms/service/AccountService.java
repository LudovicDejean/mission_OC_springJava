package com.openclassrooms.service;

import com.openclassrooms.dto.RegisterDTO;
import com.openclassrooms.exception.UserAlreadyExistException;
import com.openclassrooms.model.User;

public interface AccountService {
	User save(RegisterDTO registerDto) throws UserAlreadyExistException;

}
