package az.code.touragent.dtos;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RegisterDto {
    @NotBlank
    @Email
    private String email;
    @NotNull
    @Size(min = 2, message = "Firstname must be at least 2 character long")
    private String firstName;
    @NotNull
    @Size(min = 2, message = "Lastname must be at least 2 character long")
    private String lastName;
    @NotNull
    @Size(min = 2, message = "Company name must be at least 2 character long")
    private String companyName;
    @Size(min = 10, max =10, message = "Voen must be 10 character long")
    private String voen;
    @NotNull
    @Size(min = 8, message = "Username must be at least 8 character long")
    private String password;
}
