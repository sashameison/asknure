package asknure.narozhnyi.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InternalServiceErrorException extends ResponseStatusException {

  public InternalServiceErrorException() {
    super(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public InternalServiceErrorException(String message) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, message);
  }
}
