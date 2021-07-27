package az.code.touragent.dtos;

import az.code.touragent.models.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
    private String fullName;
    private String email;
    private String voen;

    public UserDto(User data) {
        this.fullName = data.getFirstName() + " " + data.getLastName();
        this.email = data.getEmail();
        this.voen = data.getVoen();
    }
}
