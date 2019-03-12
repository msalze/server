package ch.uzh.ifi.seal.soprafs19.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User does not exist")
public class UserDoesNotExistException extends RuntimeException {
}
