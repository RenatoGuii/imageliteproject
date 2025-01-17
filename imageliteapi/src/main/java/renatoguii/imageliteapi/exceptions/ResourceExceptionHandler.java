package renatoguii.imageliteapi.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandartError> invalidInformation(HttpServletRequest request) {
        StandartError err = new StandartError();
        err.setTimeStamp(Instant.now());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setError("Invalid Values");
        err.setMsg("Invalid or null values");
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandartError> recordNotFound(EntityNotFoundException e, HttpServletRequest request) {
        StandartError err = new StandartError();
        err.setTimeStamp(Instant.now());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setError("Not found");
        err.setMsg(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(CustomAuthenticationException.class)
    public ResponseEntity<StandartError> authenticationError(CustomAuthenticationException e, HttpServletRequest request) {
        StandartError err = new StandartError();
        err.setTimeStamp(Instant.now());
        err.setStatus(HttpStatus.UNAUTHORIZED.value());
        err.setError("Authentication error");
        err.setMsg(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
    }

    @ExceptionHandler(RegisterException.class)
    public ResponseEntity<StandartError> RegisterError(RegisterException e, HttpServletRequest request) {
        StandartError err = new StandartError();
        err.setTimeStamp(Instant.now());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setError("Erroneous no registration");
        err.setMsg(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<StandartError> TokenError(InvalidTokenException e, HttpServletRequest request) {
        StandartError err = new StandartError();
        err.setTimeStamp(Instant.now());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setError("Invalid Token");
        err.setMsg(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

}
