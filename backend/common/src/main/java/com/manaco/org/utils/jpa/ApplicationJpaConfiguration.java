package com.manaco.org.utils.jpa;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Responsible for configuring JPA properties Application database.
 *
 * @author Jhonatan Mamani
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "appEntityManagerFactory",
        basePackages = {"com.manaco.org.application"},
        transactionManagerRef = "appTransactionManager"
)
public class ApplicationJpaConfiguration {

    public static final String APPLICATION_ENTITY_MANAGER_FACTORY = "appEntityManagerFactory";
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationJpaConfiguration.class);
    private static final String APPLICATION_DATA_SOURCE = "appDataSource";
    private static final String APPLICATION_CONTAINER_ENTITY_MANAGER_FACTORY = "appContainerEntityManagerFactory";
    private static final String APPLICATION_TRANSACTION_MANAGER = "appTransactionManager";
    private static final String PACKAGE = "com.manaco.org.model";
    private static final String PERSISTENCE_NAME = "application-jpa";
    private static final String PREFIX = "application.datasource";

    @Bean(name = APPLICATION_DATA_SOURCE)
    @ConfigurationProperties(prefix = PREFIX)
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = APPLICATION_CONTAINER_ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean
    appEntityManagerFactory(@Qualifier(value = APPLICATION_DATA_SOURCE) DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
        result.setDataSource(dataSource);
        result.setPackagesToScan(PACKAGE);
        result.setPersistenceUnitName(PERSISTENCE_NAME);
        result.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        LOGGER.info("Set Data Source to Entity Manager Factory: {}", result.getDataSource());
        return result;
    }

    @Bean(name = APPLICATION_ENTITY_MANAGER_FACTORY)
    public EntityManagerFactory entityManagerFactory(
            @Qualifier(value = APPLICATION_CONTAINER_ENTITY_MANAGER_FACTORY) LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return entityManagerFactoryBean.getObject();
    }

    @Bean(name = APPLICATION_TRANSACTION_MANAGER)
    public PlatformTransactionManager appTransactionManager(
            @Qualifier(APPLICATION_ENTITY_MANAGER_FACTORY) EntityManagerFactory appEntityManagerFactory) {
        return new JpaTransactionManager(appEntityManagerFactory);
    }

}

