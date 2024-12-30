package com.StudySecurity.Service.impl;

import com.StudySecurity.Entity.User;
import com.StudySecurity.Entity.enums.Roles;
import com.StudySecurity.Exception.APIException;
import com.StudySecurity.Payload.LoginDTO;
import com.StudySecurity.Payload.RegisterDTO;
import com.StudySecurity.Repository.UserRepository;
import com.StudySecurity.Security.JwtTokenProvider;
import com.StudySecurity.Service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginDTO loginDTO) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        return jwtTokenProvider.generateJwtToken(authenticate);
    }

    @Override
    public String registerUser(RegisterDTO registerDTO) {

        // Check if username already exist or not
        if (this.userRepository.existsUserByUsername(registerDTO.getUsername()))
            throw new APIException(HttpStatus.BAD_REQUEST, "Username is already taken");

        User user = new User();
        user.setFullName(registerDTO.getFullName());
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRoles(Roles.USER);

        this.userRepository.save(user);

        return "User registered successfully";
    }
}
