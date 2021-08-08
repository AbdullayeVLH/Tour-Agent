package az.code.touragent.dtos;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AcceptedOfferDto {
    private Map<String, String> data;
    private String contactInfo;
    private MakeOfferDto offer;
}
