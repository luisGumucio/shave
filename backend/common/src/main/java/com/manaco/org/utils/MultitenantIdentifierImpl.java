package com.manaco.org.utils;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

/**
 * Responsible for resolving the current tenant identifier.
 *
 * @author Jhonatan Mamani
 */
@Component
public class MultitenantIdentifierImpl implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
        return TenantContext.getCurrentTenant();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return Boolean.TRUE;
    }
}
