package lk.uwu.lms_backend.exceptions;

import lk.uwu.lms_backend.dtos.ErrorResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Handle all uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleAllExceptions(Exception ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                500,
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return ResponseEntity.status(500).body(errorResponse);
    }

    // Handle User Already Exists Exception
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                409,
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return ResponseEntity.status(400).body(errorResponse);
    }

    // Handle User Credentials Invalid Exception
    @ExceptionHandler(UserCredentialsInvalidException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserCredentialsInvalidException(UserCredentialsInvalidException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                401,
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return ResponseEntity.status(401).body(errorResponse);
    }

    // Handle User Not Found Exception
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                404,
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return ResponseEntity.status(404).body(errorResponse);
    }
}
