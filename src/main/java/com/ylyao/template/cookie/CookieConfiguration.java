package com.ylyao.template.cookie;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author jsonqiao
 * @date 2015-12-28
 */
public class CookieConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CookieConfiguration.class);

    private static final Integer DEFAULT_EXPIRED_TIME = -1;

    private static final Integer DEFAULT_RANDOM_CHAR = 0;

    private static final String DEFAULT_PATH = "/";

    private static boolean HTTP_ONLY_SUPPORT = false;

    private static Pattern IPV4_PATTERN;

    private String name;

    private String clientName;

    private String path;

    private String domain;

    private Crypto crypto;

    private boolean httpOnly = false;

    private Integer expiredTime = DEFAULT_EXPIRED_TIME;

    private Integer randomChar = DEFAULT_RANDOM_CHAR;

    static {
        try {
            Method m = Cookie.class.getDeclaredMethod("isHttpOnly", new Class[0]);
            if (m != null) {
                HTTP_ONLY_SUPPORT = true;
            }
        } catch (Exception e) {
            logger.error("cookie configuration reflection method[Cookie.isHttpOnly] invoke exception, exception " +
                    "detail {}", e);
        }

        IPV4_PATTERN = Pattern.compile("^\\.?((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}" +
                "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)$");
    }

    public Cookie getCookie(String cookieValue) {
        Cookie c = new Cookie(getClientName(), getClientValue(cookieValue));
        c.setPath(StringUtils.isBlank(path) ? DEFAULT_PATH : path);
        if (StringUtils.isNotBlank(cookieValue)) {
            c.setMaxAge(expiredTime == null ? DEFAULT_EXPIRED_TIME : expiredTime);
        } else {    // 删除cookie
            c.setMaxAge(0);
        }
        if (setDomain()) {
            c.setDomain(domain);
        }
        if (this.isSetHttpOnly()) {
            c.setHttpOnly(true);
        }
        return c;
    }

    protected boolean setDomain() {
        return StringUtils.isNotBlank(domain)
                && !".localhost".equals(domain.toLowerCase())
                && !"localhost".equals(domain.toLowerCase());
    }

    protected boolean isSetHttpOnly() {
        return this.httpOnly && HTTP_ONLY_SUPPORT;
    }

    public String getUncryptValue(String encryptedValue) {
        if (StringUtils.isBlank(encryptedValue)) {
            return StringUtils.EMPTY;
        }
        String decryptedValue = encryptedValue;
        if (crypto != null) {
            decryptedValue = crypto.decrypt(encryptedValue);
        }
        if (randomChar.intValue() > 0) {
            decryptedValue = decryptedValue.substring(0, (decryptedValue.length() - randomChar));
        }
        return decryptedValue;
    }

    private String getClientValue(String cookieValue) {
        if (StringUtils.isBlank(cookieValue)) {
            return null;
        }
        StringBuilder clientValue = new StringBuilder(cookieValue);
        if (randomChar.intValue() > 0) {
            clientValue = clientValue.append(RandomStringUtils.random(randomChar.intValue()));
        }
        // 是否加密
        if (crypto != null) {
            return crypto.encrypt(clientValue.toString());
        }
        return clientValue.toString();
    }

    public String getClientName() {
        return clientName;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getDomain() {
        return domain;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public Integer getExpiredTime() {
        return expiredTime;
    }

    public Integer getRandomChar() {
        return randomChar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public void setExpiredTime(Integer expiredTime) {
        this.expiredTime = expiredTime;
    }

    public void setRandomChar(Integer randomChar) {
        this.randomChar = randomChar;
    }
}
