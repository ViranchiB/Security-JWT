package com.StudySecurity.Controller;

import com.StudySecurity.Payload.JwtAuthResponse;
import com.StudySecurity.Payload.LoginDTO;
import com.StudySecurity.Payload.RegisterDTO;
import com.StudySecurity.Service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDTO loginDTO) {

        // it will generate a token
        String token = this.authService.login(loginDTO);

        // We have to send the JwtAuthResponse so we are setting the token to that response DTO
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }

    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> registerUser(@RequestBody RegisterDTO registerDTO) {
        String registerUser = this.authService.registerUser(registerDTO);
        return new ResponseEntity<>(registerUser, HttpStatus.CREATED);
    }
}
