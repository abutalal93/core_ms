package restaurant.ms.core.configrations;

import org.springframework.http.HttpStatus;

import java.util.Locale;

public class MessageEnvelope {

    private Integer status;
    private String message;
    private Object data;

    public MessageEnvelope(Integer status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public MessageEnvelope() {
    }

    public MessageEnvelope(HttpStatus httpStatus, String key, Object data, Locale locale) {
        this.status = httpStatus.value();
        this.message = Utf8ResourceBundle.getString(key, locale);
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
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
