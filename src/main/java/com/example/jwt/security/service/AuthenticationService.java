package com.example.jwt.security.service;

import com.example.jwt.security.dto.AuthenticationRequest;
import com.example.jwt.security.dto.AuthenticationResponse;
import com.example.jwt.security.dto.RegisterRequest;
import com.example.jwt.security.model.Role;
import com.example.jwt.security.model.User;
import com.example.jwt.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user= User
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwt=jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwt)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user=userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        log.info(user);
        var jwt=jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwt)
                .build();
    }
}
