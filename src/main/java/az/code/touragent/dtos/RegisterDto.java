package az.code.touragent.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RegisterDto {
    private String email;
    private String firstName;
    private String lastName;
    private String companyName;
    private String voen;
    private String password;
}
