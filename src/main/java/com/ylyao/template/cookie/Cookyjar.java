package com.ylyao.template.cookie;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 封装底层cookie的接口
 *
 * @author jsonqiao
 *
 * @date 2015-12-28
 */
public interface Cookyjar {

    String COOKYJAR_IN_REQUEST = "cookyjar";


    /**
     * 提交缓存中的cookie信息
     *
     * @param httpServletResponse
     */
    public void commit(HttpServletResponse httpServletResponse);

    /**
     * 获取所有的cookie信息
     * @return
     */
    public List<String> getCookieNames();

    /**
     * 设置cookie
     *
     * @param clientName
     *
     * @param value
     */
    public void set(String clientName, String value);

    /**
     * 设置cookie
     *
     * @param clientName
     *
     * @param cookieBean
     */
    public void set(String clientName, CookieSerializable cookieBean);


    /**
     * 获取cookie信息
     *
     * @param clientName
     *
     * @return
     */
    public String get(String clientName);

    /**
     * 获取cookie并将其反序列
     *
     * @param clientName
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(String clientName, Class<T> clazz);

    /**
     * 移除cookie
     * @param clientName
     * @return
     */
    String remove(String clientName);
}
