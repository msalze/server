package ch.uzh.ifi.seal.soprafs19.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NON_AUTHORITATIVE_INFORMATION, reason = "Wrong Login Information")
public class WrongLoginException extends RuntimeException {
}
