package com.bank_transactions.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/transactions/**").permitAll()
            .requestMatchers("/cards/**").permitAll()
            .requestMatchers("/beneficiaries/**").permitAll()
            .requestMatchers("/alerts/**").permitAll()
            .requestMatchers("/auditlogs/**").permitAll()
            .requestMatchers("/customers/**").permitAll()
            .requestMatchers("/accounts/**").permitAll()
            .requestMatchers("/loans/**").permitAll()
            .requestMatchers("/paymentplans/**").permitAll()
            .requestMatchers("/exchangerates/**").permitAll() 
            .anyRequest().authenticated()
            .and()
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
}
