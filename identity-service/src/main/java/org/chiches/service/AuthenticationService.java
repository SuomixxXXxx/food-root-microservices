package org.chiches.service;

import org.chiches.dto.TokenDTO;
import org.chiches.dto.UserDTO;

public interface AuthenticationService {
    TokenDTO authenticate(String email, String password);

    TokenDTO register(UserDTO userDto);

    String serviceToken(String service,String secret);
}
