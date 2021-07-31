package az.code.touragent.models;

import lombok.*;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "archives")
@Proxy(lazy = false)
public class Archive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userEmail;
    private UUID requestId;
    private String status;
}
