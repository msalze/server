package ch.uzh.ifi.seal.soprafs19.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Wrong Login Information")
public class WrongLoginException extends RuntimeException {
}
