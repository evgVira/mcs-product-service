package org.mcs.mcsproductservice.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NpeExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public String npeExceptionHandle(NullPointerException nullPointerException){
        return nullPointerException.getMessage();
    }
}
