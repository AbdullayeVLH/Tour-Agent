package az.code.touragent.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AlreadyArchived extends RuntimeException {
    public AlreadyArchived() {
        super("This request is already archived.");
    }
}
