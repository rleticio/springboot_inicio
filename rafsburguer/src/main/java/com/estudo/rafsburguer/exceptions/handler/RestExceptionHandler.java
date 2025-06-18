package com.estudo.rafsburguer.exceptions.handler;

import com.estudo.rafsburguer.exceptions.OrderNotFoundException;
import com.estudo.rafsburguer.exceptions.ProductNotFoundException;
import com.estudo.rafsburguer.exceptions.ProductVariationNotFoundException;
import com.estudo.rafsburguer.exceptions.ProductVariationUnavailableException;
import com.estudo.rafsburguer.exceptions.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> genericException(Exception ex) {
        ApiError apiError = ApiError
                .builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .errors(List.of(ex.getMessage()))
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({OrderNotFoundException.class,
            ProductNotFoundException.class,
            ProductVariationNotFoundException.class,
    })
    public ResponseEntity<ApiError> notFoundException(RuntimeException ex){
        ApiError apiError = ApiError
                .builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND.name())
                .errors(List.of(ex.getMessage()))
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductVariationUnavailableException.class)
    public ResponseEntity<ApiError> unavailableException(RuntimeException ex){
        ApiError apiError = ApiError
            .builder()
            .timestamp(LocalDateTime.now())
            .code(HttpStatus.CONFLICT.value())
            .status(HttpStatus.CONFLICT.name())
            .errors(List.of(ex.getMessage()))
            .build();
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    //MethodArgumentNotValidException é uma exceção lançada pelo Spring ‘Framework’ quando a validação de um argumento anotado com @Valid falha. Isso geralmente ocorre durante o processo de desserialização, quando um cliente envia uma solicitação para um endpoint REST do Spring e os dados não atendem aos critérios de validação definidos no objeto de transferência de dados (DTO).
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> argumentNotValidException(MethodArgumentNotValidException ex){

        List<String> errorList = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        ApiError apiError = ApiError
                .builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.name())
                .errors(errorList)
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

}
