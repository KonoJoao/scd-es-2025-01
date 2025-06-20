package com.ecommerce.inventory.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@PropertySource("classpath:db.properties")
public class DatabaseConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSource = DataSourceBuilder.create();
        dataSource.driverClassName(Objects.requireNonNull(env.getProperty("db.driver")));
        dataSource.url(env.getProperty("db.url"));
        dataSource.username(env.getProperty("db.username"));
        dataSource.password(env.getProperty("db.password"));
        return dataSource.build();
    }
}