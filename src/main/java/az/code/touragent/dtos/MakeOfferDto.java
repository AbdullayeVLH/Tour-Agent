package az.code.touragent.dtos;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MakeOfferDto {
    @NotNull
    @Size(min = 5, message = "This field must contain minimum 5 character")
    private String price;
    @NotNull
    @Size(min = 5, message = "This field must contain minimum 5 character")
    private String dateInterval;
    @NotNull
    @Size(min = 5, message = "This field must contain minimum 5 character")
    private String tourInformation;
}
