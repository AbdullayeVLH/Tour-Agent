package az.code.touragent.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserData {
    private String email;
    private String firstName;
    private String lastName;
    private String voen;
    private LocalDateTime registrationTime;
}
