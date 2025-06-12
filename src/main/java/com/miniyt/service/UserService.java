package com.miniyt.service;

import com.miniyt.dto.request.LoginRequest;
import com.miniyt.dto.request.RegisterRequest;
import com.miniyt.exception.AttributeAlreadyExistsException;
import com.miniyt.exception.IncorrectAttributeException;
import com.miniyt.exception.NonExistentEntityException;
import com.miniyt.exception.ShortAttributeException;
import com.miniyt.model.User;
import com.miniyt.repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String login(LoginRequest loginRequest) throws NonExistentEntityException {
        User currentUser = userRepo.findByUsername(loginRequest.username())
                .orElseThrow(() -> new IncorrectAttributeException("Incorrect credentials"));

        if (!passwordEncoder.matches(loginRequest.password(), currentUser.getPassword())) {
            throw new IncorrectAttributeException("Incorrect credentials");
        }

        return jwtService
                .generateToken(currentUser.getUsername(), currentUser.getRole().toString(), currentUser.getEmail());
    }

    public String register(RegisterRequest registerRequest) {
        if (userRepo.findByUsername(registerRequest.username()).isPresent())
            throw new AttributeAlreadyExistsException("This username is taken");

        if (userRepo.findByEmail(registerRequest.email()).isPresent())
            throw new AttributeAlreadyExistsException("This email is taken");

        if (registerRequest.password().length() < 5)
            throw new ShortAttributeException("The password must be more than 5 characters");

        User newUser = new User();
        newUser.setUsername(registerRequest.username());
        newUser.setEmail(registerRequest.email());
        newUser.setRole(User.Role.USER);
        newUser.setPassword(passwordEncoder.encode(registerRequest.password()));

        userRepo.save(newUser);

        return "User registered successfully";
    }
}
