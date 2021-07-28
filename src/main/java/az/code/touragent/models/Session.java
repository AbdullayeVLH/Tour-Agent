package az.code.touragent.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;


import javax.persistence.Id;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class Session implements Serializable {
    @Id
    private UUID sessionId;
    private Long chatId;
    private String lang;
    private Map<String, String> data;

    private Action action;

    public Session() {
        this.data=new HashMap<>();
    }

    public void setData(String key, String answer) {
        data.put(key, answer);
    }
}
