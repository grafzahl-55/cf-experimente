package de.escriba.experimental.cf.cdr.datasource;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 *
 * @author micha
 */
@Configuration
@ConditionalOnMissingBean(DataSource.class)
public class DefaultDataSourceConfiguration {
    
    
    
    @Bean
    public DataSource dataSource(){
        EmbeddedDatabaseBuilder dbBuilder= new EmbeddedDatabaseBuilder();
        dbBuilder.setType(EmbeddedDatabaseType.H2);
        return dbBuilder.build();
    }
    
}
