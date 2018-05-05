package de.escriba.experimental.cf.cdrmongo.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;
import reactor.core.publisher.Mono;


@ControllerAdvice
@Slf4j
public class ExceptionHandling extends ResponseStatusExceptionHandler{


    @Override
    @ExceptionHandler
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.debug("Caught exception {}",ex);
        return super.handle(exchange, ex);
    }
}
