package az.code.touragent.security;

import az.code.touragent.dtos.LoginDto;
import az.code.touragent.dtos.LoginResponseDto;
import az.code.touragent.dtos.RegisterDto;
import az.code.touragent.dtos.RegisterResponseDto;
import org.keycloak.representations.AccessTokenResponse;

import javax.security.auth.login.LoginException;

public interface SecurityService {

    AccessTokenResponse login(LoginDto user) throws LoginException;

    RegisterResponseDto register(RegisterDto register);

    String verify(String token, String username);
}
