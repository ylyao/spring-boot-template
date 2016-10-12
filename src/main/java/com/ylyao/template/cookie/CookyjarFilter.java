package com.ylyao.template.cookie;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author jsonqiao
 * @date 2015-12-28
 */
@WebFilter
public class CookyjarFilter extends GenericFilterBean implements Filter {

    private CookyjarConfiguration cookyjarConfiguration;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        Cookyjar cookyjar = (Cookyjar) request.getAttribute(Cookyjar.COOKYJAR_IN_REQUEST);
        // to next filter
        if (cookyjar != null) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        cookyjar = CookyjarFactory.build(cookyjarConfiguration, httpServletRequest, httpServletResponse);

        initRequest(httpServletRequest, cookyjar);

        HttpServletResponse responseWrapper = new CookyjarResponseWrapper(httpServletResponse, cookyjar);

        try {
            chain.doFilter(httpServletRequest, responseWrapper);
        } finally {
            cookyjar.commit(httpServletResponse);
            cleanRequest(httpServletRequest, cookyjar);
        }

    }

    protected void initRequest(HttpServletRequest request, Cookyjar cookyjar) {
        request.setAttribute(Cookyjar.COOKYJAR_IN_REQUEST, cookyjar);
        List<String> cookieNames = cookyjar.getCookieNames();
        if (cookieNames != null) {
            cookieNames.forEach((cookieName) -> {
                request.setAttribute(cookieName, cookyjar.get(cookieName));
            });
        }
    }

    protected void cleanRequest(HttpServletRequest request, Cookyjar cookyjar) {
        request.removeAttribute(Cookyjar.COOKYJAR_IN_REQUEST);
        List<String> cookieNames = cookyjar.getCookieNames();
        if (cookieNames != null) {
            cookieNames.forEach((cookieName) -> {
                request.removeAttribute(cookieName);
            });
        }
    }

    public void setCookyjarConfiguration(CookyjarConfiguration cookyjarConfiguration) {
        this.cookyjarConfiguration = cookyjarConfiguration;
    }
}
