package com.miniyt.service;

import com.miniyt.dto.request.LoginRequest;
import com.miniyt.dto.request.RegisterRequest;
import com.miniyt.exception.AttributeAlreadyExistsException;
import com.miniyt.exception.IncorrectAttributeException;
import com.miniyt.exception.NonExistentEntityException;
import com.miniyt.exception.ShortAttributeException;
import com.miniyt.entity.User;
import com.miniyt.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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
        log.info("Registering user with username: {}", registerRequest.username());
        if (userRepo.findByUsername(registerRequest.username()).isPresent())
            throw new AttributeAlreadyExistsException("This username is taken");

        if (userRepo.findByEmail(registerRequest.email()).isPresent())
            throw new AttributeAlreadyExistsException("This email is taken");

        if (registerRequest.password().length() < 5)
            throw new ShortAttributeException("The password must be more than 5 characters");

        log.info("Errores pasados");

        User newUser = new User();
        newUser.setUsername(registerRequest.username());
        newUser.setEmail(registerRequest.email());
        newUser.setRole(User.Role.USER);
        newUser.setPassword(passwordEncoder.encode(registerRequest.password()));

        log.info("Usuario creado: {}", newUser.getUsername());

        userRepo.save(newUser);

        log.info("Usuario guardado en la base de datos: {}", newUser.getUsername());

        return "User registered successfully";
    }
}
