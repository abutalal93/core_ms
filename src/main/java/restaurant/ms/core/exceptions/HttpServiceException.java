package restaurant.ms.core.exceptions;

import org.springframework.http.HttpStatus;
import restaurant.ms.core.configrations.Utf8ResourceBundle;

import java.util.Locale;

public class HttpServiceException extends RuntimeException {

    private HttpStatus httpStatus;
    private String message;
    private Object data;

    public HttpServiceException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpServiceException(HttpStatus httpStatus, String messageKey, Locale locale) {
        super(messageKey);
        String text = Utf8ResourceBundle.getString(messageKey,locale);
        this.httpStatus = httpStatus;
        this.message = text;
    }

    public HttpServiceException(HttpStatus httpStatus, String messageKey, Object data, Locale locale) {
        super(messageKey);
        String text = Utf8ResourceBundle.getString(messageKey,locale);
        this.httpStatus = httpStatus;
        this.message = text;
        this.data = data;
    }



    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
