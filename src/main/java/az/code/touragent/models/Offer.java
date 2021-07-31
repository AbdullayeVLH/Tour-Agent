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
@Table(name = "offers")
@Proxy(lazy = false)
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String price;
    private String dateInterval;
    private String tourInformation;
    private UUID requestId;
}
