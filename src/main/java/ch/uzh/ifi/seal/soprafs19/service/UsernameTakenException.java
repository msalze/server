package ch.uzh.ifi.seal.soprafs19.service;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Username taken")
public class UsernameTakenException extends RuntimeException {

}
