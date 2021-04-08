package com.qelery.TaskRestApi.service;

import com.qelery.TaskRestApi.exception.EmailAddressExistsException;
import com.qelery.TaskRestApi.exception.UsernameExistsException;
import com.qelery.TaskRestApi.model.User;
import com.qelery.TaskRestApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {

            throw new UsernameExistsException(user.getUsername());
        } else if (userRepository.existsByEmailAddress(user.getEmailAddress())) {

            throw new EmailAddressExistsException(user.getEmailAddress());
        } else {

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
