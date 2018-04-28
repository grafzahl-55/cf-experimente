package de.escriba.experimental.cf.cdr;
import de.escriba.experimental.cf.cdr.model.ContextDefinitionEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@ComponentScan(basePackageClasses = CdrApplication.class)
public class CdrApplication {
    
        
    
    
	public static void main(String[] args) {
		SpringApplication.run(CdrApplication.class, args);
	}
        

        
}
