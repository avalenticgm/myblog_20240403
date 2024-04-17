package it.cgmconsulting.myblog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class GenericException extends RuntimeException{

    private String msg;
    public GenericException(String msg) {
        super(msg);
    }
}
