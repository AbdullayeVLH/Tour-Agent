package az.code.touragent.controllers;

import az.code.touragent.dtos.LoginDto;
import az.code.touragent.dtos.RegisterDto;
import az.code.touragent.exceptions.EmailNotVerified;
import az.code.touragent.security.SecurityService;
import az.code.touragent.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;

@RestController
@RequestMapping ("/auth")
public class UserController {

    UserService userService;
    SecurityService securityService;

    public UserController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> handleNotFound(LoginException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailNotVerified.class)
    public ResponseEntity<String> handleNotFound(EmailNotVerified e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) throws LoginException {
        return ResponseEntity.ok(securityService.login(loginDto));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        return ResponseEntity.ok(securityService.register(registerDto));
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String token, @RequestParam String username) {
        return ResponseEntity.ok(securityService.verify(token, username));
    }
}
