package az.code.touragent.models;

import az.code.touragent.dtos.RegisterDto;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "users")
public class User {
    @Id
    private String email;
    private String firstName;
    private String lastName;
    private String companyName;
    private String voen;

    public User(RegisterDto register) {
        this.email = register.getEmail();
        this.firstName = register.getFirstName();
        this.lastName = register.getLastName();
        this.companyName = register.getCompanyName();
        this.voen = register.getVoen();
    }
}
