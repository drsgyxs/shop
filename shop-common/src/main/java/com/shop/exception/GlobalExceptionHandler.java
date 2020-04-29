package com.shop.exception;

import com.shop.bean.ResponseBody;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Iterator;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseBody validationExceptionHandler(ValidationException e) {
        String message = null;
        if(e instanceof ConstraintViolationException){
            ConstraintViolationException exception = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
            Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();
            message = iterator.next().getMessage();
        }
        return ResponseBody.error(message);
    }

    @ExceptionHandler(BindException.class)
    public ResponseBody validExceptionHandler(BindException e){
        FieldError fieldError = e.getBindingResult().getFieldError();
        assert fieldError != null;
        return ResponseBody.error(fieldError.getDefaultMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validExceptionHandler(MethodArgumentNotValidException e){
        FieldError fieldError = e.getBindingResult().getFieldError();
        assert fieldError != null;
        return ResponseBody.error(fieldError.getDefaultMessage());
    }

    @ExceptionHandler(BasicException.class)
    public Object exceptionHandler(BasicException e) {
        return ResponseBody.error(e.getMessage());
    }
}
