package az.code.touragent.dtos;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class UserData {
    @NotBlank
    @Email
    private String email;
    @NotNull
    @Size(min = 2, message = "Firstname must be at least 2 character long")
    private String firstName;
    @NotNull
    @Size(min = 2, message = "Lastname must be at least 2 character long")
    private String lastName;
    @Size(min = 10, max =10, message = "Voen must be 10 character long")
    private String voen;
    @FutureOrPresent
    private LocalDateTime registrationTime;
}
