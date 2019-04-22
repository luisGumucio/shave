package com.manaco.org.utils.interceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manaco.org.application.ApplicationService;
import com.manaco.org.utils.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Responsible for managing request so that it obtains the tenant's ID header or
 * application's ID.
 *
 * @author Jhonatan Mamani
 */
@Component
public class TenantInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantInterceptor.class);
    private static final String UUID_PATTERN
            = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";
    private static final Map<String, String> APPLICATIONS_CACHE = new HashMap<>();
    private static final String EMPTY_STRING = "";

    @Autowired
    private ApplicationService appService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String tenantId = request.getHeader(TenantContext.TENANT_ID_HEADER);
        if (!StringUtils.isEmpty(tenantId)) {
            String tenant = getValidTenantId(tenantId);
            if (!StringUtils.isEmpty(tenant)) {
                TenantContext.setCurrentTenant(tenant);
                LOGGER.info("Tenant ID was setted to {}", tenant);
                return Boolean.TRUE;
            }
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter()
                .write("{\"error\": \"The resource could not be found / on this server.\"}");
        response.getWriter().close();
        LOGGER.info("Invalid tenant ID {}", tenantId);
        return Boolean.FALSE;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        TenantContext.clear();
    }

    private String getValidTenantId(String tenantId) {
        if (Pattern.matches(UUID_PATTERN, tenantId)) {
            if (APPLICATIONS_CACHE.containsKey(tenantId)) {
                return APPLICATIONS_CACHE.get(tenantId);
            }
            String applicationName = appService.findApplicationNameById(UUID.fromString(tenantId));
            if (!StringUtils.isEmpty(applicationName)) {
                APPLICATIONS_CACHE.put(tenantId, applicationName);
                return applicationName;
            }
        } else {
            if (APPLICATIONS_CACHE.containsValue(tenantId)) {
                return tenantId;
            }
            UUID applicationId = appService.findApplicationIdByName(tenantId);
            if (!StringUtils.isEmpty(applicationId)) {
                APPLICATIONS_CACHE.put(applicationId.toString(), tenantId);
                return tenantId;
            }
        }
        return EMPTY_STRING;
    }
}

