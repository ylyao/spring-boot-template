package com.ylyao.template.cookie;

/**
 * @author jsonqiao
 *
 * @date 2015-12-28
 */
public enum EnumCookieType {

    USER_COOKIE("user_cookie", "_su_", "用户信息cookie");

    private String name;

    private String clientName;

    private String Description;

    EnumCookieType(String name, String clientName, String description) {
        this.name = name;
        this.clientName = clientName;
        Description = description;
    }

    public String getName() {
        return name;
    }

    public String getClientName() {
        return clientName;
    }

    public String getDescription() {
        return Description;
    }
}
