package com.ylyao.template.cookie;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * @author jsonqiao
 *
 * @date 2015-12-28
 */
public class CookyjarResponseWrapper extends HttpServletResponseWrapper {

    private Cookyjar cookyjar;

    public CookyjarResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    public CookyjarResponseWrapper(HttpServletResponse response, Cookyjar cookyjar) {
        this(response);
        this.cookyjar = cookyjar;
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        cookyjar.commit((HttpServletResponse) getResponse());
        super.sendRedirect(location);
    }
}
