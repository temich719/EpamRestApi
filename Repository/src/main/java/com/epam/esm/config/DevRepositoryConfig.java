package com.epam.esm.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

@Configuration
@ComponentScan("com.epam.esm")
@Profile("dev")
public class DevRepositoryConfig {

    private static final String SCHEMA = "classpath:schema.sql";
    private static final String START_DATA = "classpath:data.sql";

    @Bean
    public DataSource getDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(SCHEMA)
                .addScript(START_DATA)
                .build();
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(getDataSource());
        sessionFactory.setPackagesToScan("com.epam.esm");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        final HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        return hibernateProperties;
    }

    @Bean
    public TimeZone getTimeZone() {
        return TimeZone.getTimeZone("UTC");
    }

    @Bean
    public DateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
    }

    @Bean("date")
    @Scope("prototype")
    public Date getDate() {
        return new Date();
    }

    @Bean
    public ApplicationContext getApplicationContext() {
        return new AnnotationConfigApplicationContext();
    }

}
