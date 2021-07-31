package az.code.touragent.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AlreadyOffered extends RuntimeException {
    public AlreadyOffered() {
        super("Offer for this request is already made.");
    }
}
