package az.code.touragent.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class EmailNotVerified extends RuntimeException {
    public EmailNotVerified() {
        super("This user's email not verified.");
    }
}
