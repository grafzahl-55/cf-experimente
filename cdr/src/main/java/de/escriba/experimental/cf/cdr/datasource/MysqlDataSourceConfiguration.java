package de.escriba.experimental.cf.cdr.datasource;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@Profile("mysql")
@Data
public class MysqlDataSourceConfiguration {

    @Value("${datasource.mysql.url}")
    private String url;
    @Value("${datasource.mysql.user}")
    private String user;
    @Value("${datasource.mysql.password}")
    private String password;

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource ds=new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername(user);
        ds.setPassword(password);
        ds.setUrl(url);
        return ds;
    }
}
