package com.bank_transactions.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank_transactions.auth.dtos.UserDto;
import com.bank_transactions.auth.entities.User;
import com.bank_transactions.auth.entities.User.Role;
import com.bank_transactions.auth.entities.User.Status;
import com.bank_transactions.auth.exceptions.EmailAlreadyExistsException;
import com.bank_transactions.auth.mappers.UserMapper;
import com.bank_transactions.auth.repositories.UserRepository;

@Service
public class UserService implements UserServiceInterface{

    @Autowired private UserRepository userRepo;
    @Autowired private UserMapper userMapper;
    @Autowired private PasswordEncoder passwordEncoder;

     @Override
    public void register(UserDto dto) {
        if (userRepo.existsById(dto.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepo.save(user);
    }

     @Override
    public boolean validateCredentials(String email, String rawPassword) {
        return userRepo.findByEmail(email)
            .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
            .orElse(false);
    }

     @Override
    public boolean userExists(String email) {
    return userRepo.existsById(email);
}

 @Override
public void registerAdmin(UserDto dto) {
    if (userRepo.existsById(dto.getEmail())) {
        throw new EmailAlreadyExistsException();
    }
    User user = userMapper.toEntity(dto);
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    user.setRole(Role.ADMIN); // Ajoutez ce champ à votre entité User
    user.setStatus(Status.APPROVED);
    userRepo.save(user);
}
}
