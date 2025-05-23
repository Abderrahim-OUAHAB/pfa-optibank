package com.bank_transactions.auth.services;

import com.bank_transactions.auth.dtos.UserDto;
import com.bank_transactions.auth.exceptions.EmailAlreadyExistsException;

public interface UserServiceInterface {


    void register(UserDto dto) throws EmailAlreadyExistsException;

 
    boolean validateCredentials(String email, String rawPassword);

  
    boolean userExists(String email);

 
    void registerAdmin(UserDto dto) throws EmailAlreadyExistsException;
}