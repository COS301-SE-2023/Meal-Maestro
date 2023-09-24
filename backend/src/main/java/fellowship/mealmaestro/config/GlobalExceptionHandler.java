package fellowship.mealmaestro.config;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler(RuntimeException.class)
    // public ResponseEntity<String> handleUserNotFoundException(RuntimeException e)
    // {
    // return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    // }
}
