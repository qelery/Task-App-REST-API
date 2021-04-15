package com.qelery.TaskRestApi.service;

import com.qelery.TaskRestApi.exception.EmailExistsException;
import com.qelery.TaskRestApi.model.User;
import com.qelery.TaskRestApi.model.login.LoginRequest;
import com.qelery.TaskRestApi.model.login.LoginResponse;
import com.qelery.TaskRestApi.repository.UserRepository;
import com.qelery.TaskRestApi.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserDetailsService userDetailsService,
                       JwtUtils jwtUtils,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<?> createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailExistsException(user.getEmail());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            String message = "Successfully registered new user with email address " + user.getEmail();
            return ResponseEntity.ok(message);
        }
    }

    public ResponseEntity<?> loginUser(LoginRequest loginRequest) throws BadCredentialsException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String JWT = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponse(JWT));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}