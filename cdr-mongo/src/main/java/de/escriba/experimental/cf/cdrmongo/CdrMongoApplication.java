package de.escriba.experimental.cf.cdrmongo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class CdrMongoApplication {

    public static void main(String[] args){
        SpringApplication.run(CdrMongoApplication.class,args);
    }



}
