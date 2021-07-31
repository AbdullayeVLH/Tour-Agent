package az.code.touragent.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RequestExpired extends RuntimeException {
    public RequestExpired() {
        super("The request expired or deleted.");
    }
}

