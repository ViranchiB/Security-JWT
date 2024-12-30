package com.StudySecurity.Service;

import com.StudySecurity.Payload.LoginDTO;
import com.StudySecurity.Payload.RegisterDTO;

public interface AuthService {
    String login(LoginDTO loginDTO);
    String registerUser(RegisterDTO registerDTO);
}
