package az.code.touragent.models;

import az.code.touragent.enums.RequestStatus;
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
@Table(name = "agentRequests")
@Proxy(lazy = false)
public class AgentRequests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    private String userEmail;
    private UUID requestId;

}
