package com.manaco.org.utils.jpa;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Responsible for configuring JPA multi-tenancy properties.
 *
 * @author Jhonatan Mamani
 */
@Configuration
@ComponentScan("com.manaco.org")
@EnableConfigurationProperties(JpaProperties.class)
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        basePackages = {"com.manaco.org.entries"}
)
public class MultitenantJpaConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultitenantJpaConfiguration.class);
    private static final String DATA_SOURCE = "dataSource";
    private static final String ENTITY_MANAGER_FACTORY = "entityManagerFactory";
    private static final String MULTI_TENANT_ENTITY_MANAGER_FACTORY = "multiTenantEntityManagerFactory";
    private static final String PACKAGE = "com.manaco.org.model";
    private static final String PERSISTENCE_NAME = "multitenant-jpa";
    private static final String PREFIX = "spring.datasource";

    @Autowired
    private JpaProperties jpaProperties;

    @Primary
    @Bean(name = DATA_SOURCE)
    @ConfigurationProperties(PREFIX)
    public DataSource dataSourcesOne() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = ENTITY_MANAGER_FACTORY)
    public EntityManagerFactory entityManagerFactory(
            @Qualifier(value = MULTI_TENANT_ENTITY_MANAGER_FACTORY) LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return entityManagerFactoryBean.getObject();
    }

    @Primary
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = MULTI_TENANT_ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(DataSource dataSource,
                                                                           MultiTenantConnectionProvider multiTenantConnectionProvider,
                                                                           CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {

        Map<String, Object> properties = new LinkedHashMap<>();
        properties.putAll(jpaProperties.getProperties());
        properties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        properties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);
        properties.put(Environment.ENABLE_LAZY_LOAD_NO_TRANS, true);

        LOGGER.info("Initialize JPA properties...Starting with multitenancy strategy: {}", properties.get(Environment.MULTI_TENANT));

        LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();

        result.setDataSource(dataSource);
        result.setPackagesToScan(PACKAGE);
        result.setPersistenceUnitName(PERSISTENCE_NAME);
        result.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        result.setJpaPropertyMap(properties);

        LOGGER.info("Set Data Source to Entity Manager Factory: {}", result.getDataSource());

        return result;
    }
}

