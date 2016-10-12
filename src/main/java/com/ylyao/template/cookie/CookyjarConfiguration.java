package com.ylyao.template.cookie;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jsonqiao
 *
 * @date 2015-12-28
 */
public class CookyjarConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CookyjarConfiguration.class);

    private Map<String, CookieConfiguration> cookieConfigurationCache;

    private List<CookieConfiguration> configurations;

    private CookieConfiguration defaultConfiguration;


    public void afterPropertiesSet() throws Exception {
        if (configurations == null || configurations.isEmpty()) {
            throw new NullPointerException("cookie configuration can't be null");
        }
        cookieConfigurationCache = new HashMap<>(configurations.size());
        configurations.forEach((configuration) -> {
            buildBaseConfiguration(configuration);
            customCookieConfiguration(configuration);
            cookieConfigurationCache.put(configuration.getClientName(), configuration);
        });
        logger.debug("cookie config map init completed, value is {}", cookieConfigurationCache);
    }

    private void buildBaseConfiguration(CookieConfiguration cookieConfiguration) {
        if (StringUtils.isBlank(cookieConfiguration.getName())) {
            throw new NullPointerException("cookie name can't be null");
        }
        if (StringUtils.isBlank(cookieConfiguration.getClientName())) {
            throw new NullPointerException("cookie client name can't be null");
        }
    }

    public CookieConfiguration getCookieConfiguration(String clientName) {
        return cookieConfigurationCache.get(clientName);
    }


    protected void customCookieConfiguration(CookieConfiguration cookieConfiguration) {

    }

    public void setConfigurations(List<CookieConfiguration> configurations) {
        this.configurations = configurations;
        try {
            afterPropertiesSet();
        } catch (Exception e) {
            logger.error("set cookyjar configurations exception, {}", e);
            throw new RuntimeException(e);
        }
    }

    public void setDefaultConfiguration(CookieConfiguration defaultConfiguration) {
        this.defaultConfiguration = defaultConfiguration;
    }
}
