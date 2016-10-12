package com.ylyao.template.cookie;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jsonqiao
 * @date 2015-12-28
 */
public class DefaultCookyjar implements Cookyjar {

    private static final Logger logger = LoggerFactory.getLogger(DefaultCookyjar.class);

    private CookyjarConfiguration cookyjarConfiguration;

    private HttpServletRequest request;

    private boolean modified = false;

    private Map<String, CookieValue> cookieMap = new HashMap();

    public DefaultCookyjar(CookyjarConfiguration cookyjarConfiguration, HttpServletRequest request) {
        this.cookyjarConfiguration = cookyjarConfiguration;
        this.request = request;
        initCookieCache(request);
    }

    protected void initCookieCache(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                String clientName = cookie.getName();
                CookieConfiguration configuration = cookyjarConfiguration.getCookieConfiguration(clientName);
                if (configuration == null) {
                    logCookieInfo(cookie, "unknow cookie");
                } else {
                    DefaultCookyjar.CookieValue cookieValue = new DefaultCookyjar.CookieValue();
                    cookieValue.cfg = configuration;
                    cookieValue.encrypted = cookie.getValue();
                    cookieValue.unencrypt = configuration.getUncryptValue(cookie.getValue());
                    cookieMap.put(clientName, cookieValue);
                    logCookieInfo(cookie, "know cookie with configuration");
                }

            }
        }
    }

    private void logCookieInfo(Cookie cookie, String prefix) {
        logger.debug("{}, cookie name : {}, cookie value : {}, cookie domain : {}, cookie path : {}, " +
                        "cookie expire time : {}", prefix, cookie.getName(), cookie.getValue(), cookie.getDomain(),
                cookie.getPath(), cookie.getMaxAge());
    }


    @Override
    public void set(String clientName, String value) {
        if (StringUtils.isBlank(clientName)) {
            throw new NullPointerException("cookie client name can't be null");
        }
        // 当值被情况时移除
        if (StringUtils.isBlank(value)) {
            logger.debug("remove cookie, cookie name {}", clientName);
            cookieMap.remove(clientName);
            modified = true;
        }
        CookieConfiguration configuration = cookyjarConfiguration.getCookieConfiguration(clientName);
        if (configuration == null) {
            logger.debug("can't find cookie configuration, client name : {}, value : {}", clientName, value);
        } else {
            DefaultCookyjar.CookieValue cookieValue = new DefaultCookyjar.CookieValue();
            cookieValue.cfg = configuration;
            cookieValue.modified = true;
            cookieValue.unencrypt = value;
            cookieMap.put(clientName, cookieValue);
            modified = true;
            logger.debug("find cookie configuration, client name : {}, value : {}", clientName, value);
        }
    }

    @Override
    public void set(String clientName, CookieSerializable cookieBean) {
        set(clientName, cookieBean.serializing());
    }

    @Override
    public void commit(HttpServletResponse httpServletResponse) {
        if (!modified) {
//            logger.debug("cookie not modified.");
            return;
        }
        if (httpServletResponse.isCommitted()) {
            logger.error("httpServletResponse is commited, so cookie can't write to reponse.");
            return;
        }
        cookieMap.forEach((key, value) -> {
            DefaultCookyjar.CookieValue cookieValue = value;
            if (cookieValue.modified) {
                Cookie cookie = cookieValue.cfg.getCookie(cookieValue.unencrypt);
                logger.debug("add a cookie : {}", getCookieString(cookie));
                cookieValue.encrypted = cookie.getValue();
                cookieValue.modified = false;
                httpServletResponse.addCookie(cookie);
            }
        });
    }

    @Override
    public List<String> getCookieNames() {
        List<String> cookieNames = new ArrayList<>(cookieMap.size());
        cookieMap.forEach((key, value) -> {
            cookieNames.add(key);
        });
        return cookieNames;
    }

    @Override
    public String get(String clientName) {
        DefaultCookyjar.CookieValue cookieValue = cookieMap.get(clientName);
        if (cookieValue == null) {
            logger.error("can't find cookie info, request cookie name : {}", clientName);
        }
        return cookieValue.encrypted;
    }

    @Override
    public <T> T get(String clientName, Class<T> clazz) {
        CookieValue cookieValue = cookieMap.get(clientName);
        if (cookieValue == null) {
            logger.error("can't find cookie info");
            return null;
        }
        String decryptedValue = cookieValue.unencrypt;
        if (StringUtils.isBlank(decryptedValue)) {
            decryptedValue = cookieValue.cfg.getUncryptValue(cookieValue.encrypted);
        }
        if (StringUtils.isBlank(decryptedValue)) {
            throw new NullPointerException("can't find cookie object");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(decryptedValue, clazz);
        } catch (IOException e) {
            logger.error("json string to object exception, {}", e);
        }
        return null;
    }

    @Override
    public String remove(String key) {
        if(key == null) {
            throw new NullPointerException("key can\'t be null.");
        } else {
            String value = this.get(key);
            DefaultCookyjar.CookieValue cv = this.cookieMap.get(key);
            if(cv == null) {
                return null;
            } else {
                cv.unencrypt = null;
                cv.modified = true;
                if(logger.isDebugEnabled()) {
                    logger.debug("remove value with key:" + key);
                }
                modified = true;
                return value;
            }
        }
    }

    static final class CookieValue {
        private CookieConfiguration cfg;
        private String unencrypt;
        private String encrypted;
        private boolean modified = false;

        CookieValue() {
        }
    }

    private final String getCookieString(Cookie c) {
        if (c == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer(c.toString());
        sb.append(" name[").append(c.getName()).append("] value[").append(c.getValue()).append("] domain[");
        sb.append(c.getDomain()).append("] path[").append(c.getPath()).append("] maxAge[").append(c.getMaxAge());
        sb.append("]");
        return sb.toString();
    }
}
