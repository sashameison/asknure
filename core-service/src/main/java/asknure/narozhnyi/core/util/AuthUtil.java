package asknure.narozhnyi.core.util;

import java.util.Optional;

import asknure.narozhnyi.core.model.Comment;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

@UtilityClass
public class AuthUtil {

  public void setAuthor(Comment comment) {
    Optional.ofNullable(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .filter(Authentication::isAuthenticated)
        .map(authentication -> (Jwt) authentication.getPrincipal())
        .map(jwt -> jwt.getClaimAsString("name"))
        .ifPresent(comment::setAuthor);
  }

  public String getEmail() {
    return Optional.ofNullable(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .filter(Authentication::isAuthenticated)
        .map(authentication -> (Jwt) authentication.getPrincipal())
        .map(jwt -> jwt.getClaimAsString("email"))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
  }

  public Jwt getJwt() {
    return Optional.ofNullable(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .filter(Authentication::isAuthenticated)
        .map(authentication -> (Jwt) authentication.getPrincipal())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
  }
}
