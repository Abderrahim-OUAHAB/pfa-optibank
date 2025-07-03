package com.bank_transactions.auth.mappers;

import org.springframework.stereotype.Component;
import com.bank_transactions.auth.entities.User;
import com.bank_transactions.auth.dtos.UserDto;

@Component
public class UserMapper {

    public User toEntity(UserDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setCity(dto.getCity());
        user.setPostalCode(dto.getPostalCode());
        user.setCountry(dto.getCountry());
        user.setCin(dto.getCin());
        user.setNationality(dto.getNationality());
        user.setProfession(dto.getProfession());
        user.setMonthlyIncome(dto.getMonthlyIncome());
        user.setBirthDate(dto.getBirthDate());
        user.setStatus(dto.getStatus());
        user.setRole(dto.getRole());
        return user;
    }

    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        dto.setPostalCode(user.getPostalCode());
        dto.setCountry(user.getCountry());
        dto.setCin(user.getCin());
        dto.setNationality(user.getNationality());
        dto.setProfession(user.getProfession());
        dto.setMonthlyIncome(user.getMonthlyIncome());
        dto.setBirthDate(user.getBirthDate());
        dto.setStatus(user.getStatus());
        dto.setRole(user.getRole());
        return dto;
    }
}
