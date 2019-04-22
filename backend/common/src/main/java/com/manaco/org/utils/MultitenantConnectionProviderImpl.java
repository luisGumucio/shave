package com.manaco.org.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * A specialized Connection provider contract used when the application is using
 * multi-tenancy support requiring tenant aware connections.
 *
 * @author Jhonatan Mamani
 */
@Component
public class MultitenantConnectionProviderImpl implements
        MultiTenantConnectionProvider, ServiceRegistryAwareService {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(MultitenantConnectionProviderImpl.class);

    private DataSource dataSource;

    @Override
    @SuppressWarnings("unchecked")
    public void injectServices(ServiceRegistryImplementor serviceRegistry) {
        Map<String, Object> settings
                = serviceRegistry.getService(ConfigurationService.class).getSettings();
        if (settings.get(Environment.DATASOURCE) != null) {
            dataSource = (DataSource) settings.get(Environment.DATASOURCE);
            LOGGER.debug("Get data source from settings: {}", dataSource);
        }
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
            LOGGER.debug("Release connection to Datasource");
        }
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        final Connection connection = getAnyConnection();
        LOGGER.debug("Start connection in schema: {}", connection.getSchema());
        connection.setSchema(tenantIdentifier);
        LOGGER.info("Set schema to connection as: {}", connection.getSchema());
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection)
            throws SQLException {
        this.releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return Boolean.FALSE;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        //not required
        return null;
    }
}

