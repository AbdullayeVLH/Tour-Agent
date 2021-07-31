package az.code.touragent.models;

import az.code.touragent.enums.RequestStatus;
import lombok.*;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Map;
import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "requests")
@Proxy(lazy = false)
public class Request {
    @Id
    private UUID requestId;
    private Long chatId;
    private String lang;
    private Boolean expired;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "request_data", joinColumns = @JoinColumn(name = "request_request_id"))
    private Map<String, String> data;
}
