package com.bank_transactions.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank_transactions.auth.dtos.UserDto;
import com.bank_transactions.auth.entities.User;
import com.bank_transactions.auth.exceptions.EmailAlreadyExistsException;
import com.bank_transactions.auth.mappers.UserMapper;
import com.bank_transactions.auth.repositories.UserRepository;

@Service
public class UserService {

    @Autowired private UserRepository userRepo;
    @Autowired private UserMapper userMapper;
    @Autowired private PasswordEncoder passwordEncoder;

    public void register(UserDto dto) {
        if (userRepo.existsById(dto.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepo.save(user);
    }

    public boolean validateCredentials(String email, String rawPassword) {
        return userRepo.findByEmail(email)
            .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
            .orElse(false);
    }
}
