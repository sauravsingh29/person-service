package com.test.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;

@Getter
@ToString
public class PersonServiceException extends RuntimeException {

    private HttpStatus code;

    private String source;

    private String message;

    private Throwable throwable;

    public PersonServiceException(String message, @NonNull Throwable cause, @NonNull HttpStatus code, @NonNull String source) {
        super(message, cause);
        this.code = code;
        this.source = source;
        this.message = message == null ? getRootCauseMessage(cause) : message;
    }

    public PersonServiceException(@NonNull Throwable cause, @NonNull HttpStatus code, @NonNull String source) {
        super(cause);
        this.code = code;
        this.source = source;
        this.message = getRootCauseMessage(cause);
    }

    public PersonServiceException(@NonNull String message, @NonNull HttpStatus code, @NonNull String source) {
        super(message);
        this.code = code;
        this.source = source;
        this.message = message;
    }
}
