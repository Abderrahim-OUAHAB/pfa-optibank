package com.bank_transactions.auth.config;
import com.bank_transactions.auth.dtos.UserDto;
import com.bank_transactions.auth.entities.User.Role;
import com.bank_transactions.auth.entities.User.Status;
import com.bank_transactions.auth.services.UserService;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminAccountConfig {

    @Bean
    CommandLineRunner initAdminAccount(UserService userService) {
        return args -> {
            
            String adminEmail = "ob@optibank.com";
            if (!userService.userExists(adminEmail)) {
                UserDto adminDto = new UserDto();
                adminDto.setEmail(adminEmail);
                adminDto.setPassword("optibank123!"); 
                adminDto.setRole(Role.ADMIN); 
                adminDto.setFirstName("OPTIBANK");
                adminDto.setLastName("Admin");
                adminDto.setPhone("OPTIBANK");
                adminDto.setAddress("OPTIBANK");
                adminDto.setCity("OPTIBANK");
                adminDto.setPostalCode("12345");
                adminDto.setCountry("OPTIBANK");
                adminDto.setCin("OPTIBANK");
                adminDto.setNationality("OPTIBANK");
                adminDto.setProfession("OPTIBANK");
                adminDto.setMonthlyIncome(0);
                adminDto.setBirthDate(LocalDate.now());
                adminDto.setStatus(Status.APPROVED);
                
                userService.registerAdmin(adminDto);
                System.out.println("Compte admin créé avec succès");
            }
        };
    }
}