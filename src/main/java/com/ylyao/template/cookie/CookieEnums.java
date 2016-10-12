package com.ylyao.template.cookie;

public enum CookieEnums {

    PLT_USER("plt_user", "_pu_", "平台用户信息cookie");

    private String name;

    private String clientName;

    private String Desc;

    CookieEnums(String name, String clientName, String desc) {
        this.name = name;
        this.clientName = clientName;
        Desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getClientName() {
        return clientName;
    }

    public String getDesc() {
        return Desc;
    }
}
