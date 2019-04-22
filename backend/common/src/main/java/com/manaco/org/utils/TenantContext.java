package com.manaco.org.utils;

public class TenantContext {

    public static final String TENANT_ID_HEADER = "X-TenantID";
    private static final String DEFAULT_TENANT_SCHEMA = "public";

    private static ThreadLocal<String> currentTenant = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return DEFAULT_TENANT_SCHEMA;
        }
    };

    public static void setCurrentTenant(String tenant) {
        currentTenant.set(tenant);
    }

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.remove();
    }

}
