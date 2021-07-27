package az.code.touragent.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "requests")
public class Request {
    @Id
    private Long id;
    private Long chatId;
    private String lang;
    private Boolean expired;
}
