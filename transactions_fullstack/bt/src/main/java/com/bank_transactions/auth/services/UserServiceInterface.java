package com.bank_transactions.auth.services;

import java.util.List;
import java.util.Optional;

import com.bank_transactions.auth.dtos.UserDto;
import com.bank_transactions.auth.entities.User;
import com.bank_transactions.auth.exceptions.EmailAlreadyExistsException;

public interface UserServiceInterface {


    void register(UserDto dto) throws EmailAlreadyExistsException;

 
    boolean validateCredentials(String email, String rawPassword);

    User findUserByEmail(String email);
    boolean userExists(String email);
    void deleteByEmail(String email);
    List<UserDto> getAll();
 
    void registerAdmin(UserDto dto) throws EmailAlreadyExistsException;

    void updateUserStatus(String email, String status);
}