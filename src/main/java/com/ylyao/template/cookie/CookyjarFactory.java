package com.ylyao.template.cookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jsonqiao
 *
 * @date 2015-12-28
 */
public class CookyjarFactory {

    public static final Cookyjar build(CookyjarConfiguration cookyjarConfiguration, HttpServletRequest request,
                                       HttpServletResponse response) {
        return new DefaultCookyjar(cookyjarConfiguration, request);
    }
}
