package az.code.touragent.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MakeOfferDto {
    private String price;
    private String dateInterval;
    private String tourInformation;
}
