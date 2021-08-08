package az.code.touragent.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.OK)
public class NotAnyAcceptedOffer extends RuntimeException {
    public NotAnyAcceptedOffer() {
        super("You don't have any accepted offer.");
    }
}
