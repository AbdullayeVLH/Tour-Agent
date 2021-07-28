package az.code.touragent.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Action implements Serializable {
    private Long id;
    private String actionType;
    private String actionKeyword;
    private Long baseId;
    private Long nextId;
}
