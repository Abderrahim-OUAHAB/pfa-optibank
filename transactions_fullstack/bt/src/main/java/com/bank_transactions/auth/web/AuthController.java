package com.bank_transactions.auth.web;

import java.util.Map;
import org.springframework.security.core.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank_transactions.auth.dtos.AuthRequestDto;
import com.bank_transactions.auth.dtos.JwtResponseDto;
import com.bank_transactions.auth.dtos.UserDto;
import com.bank_transactions.auth.exceptions.InvalidCredentialsException;
import com.bank_transactions.auth.security.JwtService;
import com.bank_transactions.auth.services.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin("http://localhost:4200")
public class AuthController {

    @Autowired private UserService userService;
    @Autowired private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody UserDto dto) {
        userService.register(dto);
     return ResponseEntity.ok(Map.of("message", "Account created"));

    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto>login(@RequestBody AuthRequestDto request) {
        if (userService.validateCredentials(request.getEmail(), request.getPassword())) {
            String token = jwtService.generateToken(request.getEmail());
            return ResponseEntity.ok(new JwtResponseDto(token));
        }
        throw new InvalidCredentialsException();
    }

     @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
            @RequestHeader(name = "Authorization") String authHeader,
            Authentication authentication) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // In a real implementation, you might want to blacklist the token here
            return ResponseEntity.ok(Map.of("message", "Logout successful"));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "Invalid token"));
    }
}
