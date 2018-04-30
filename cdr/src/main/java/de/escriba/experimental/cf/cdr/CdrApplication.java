package de.escriba.experimental.cf.cdr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
@ComponentScan(basePackageClasses = CdrApplication.class)
public class CdrApplication {

	public static final String REPOSITORY_NAME_CONTEXT="contexts";
    public static final String REPOSITORY_NAME_APPINSTANCE ="application-instances" ;
    public static final String REPOSITORY_NAME_DEPLOYMENENT = "deployments";

    //public static final String URL_PATTERN_SOURCES="/sources/{id}/{role}";
	// Prefix /api --> Siehe application.yml
	public static final String URL_PATTERN_SOURCES="/api/contexts/{id}/sources/{role}";


    public static void main(String[] args) {
		SpringApplication.run(CdrApplication.class, args);
	}
        

        
}
