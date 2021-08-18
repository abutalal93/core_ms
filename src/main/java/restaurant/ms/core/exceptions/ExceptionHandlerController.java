package restaurant.ms.core.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import restaurant.ms.core.configrations.MessageEnvelope;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler({HttpServiceException.class})
    public ResponseEntity<MessageEnvelope> handleHttpServiceException(HttpServiceException ex) {
        MessageEnvelope messageEnvelope = new MessageEnvelope(ex.getHttpStatus().value(), ex.getMessage(), ex.getData());
        return new ResponseEntity<>(messageEnvelope, ex.getHttpStatus());
    }
}
