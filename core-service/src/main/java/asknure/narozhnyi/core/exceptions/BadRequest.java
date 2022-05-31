package asknure.narozhnyi.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadRequest extends ResponseStatusException {

  public BadRequest() {
    super(HttpStatus.BAD_REQUEST);
  }

  public BadRequest(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }
}
