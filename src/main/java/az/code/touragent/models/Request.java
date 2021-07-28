package az.code.touragent.models;

import lombok.*;

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
@ToString
public class Request {
    @Id
    private UUID requestId;
    private Long chatId;
    private String lang;
    private Boolean expired;

    @ElementCollection
    private Map<String, String> data;
}
