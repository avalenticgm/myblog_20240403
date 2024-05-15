package it.cgmconsulting.myblog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentReportingException extends RuntimeException{

    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    public CommentReportingException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s with %s %s not found or already reported.", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
