package com.ylyao.template.config;

import com.ylyao.template.cookie.*;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaoxun on 2016/2/16.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public FilterRegistrationBean characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return new FilterRegistrationBean(
                characterEncodingFilter);
    }


    @Override
    public void configureContentNegotiation(
            ContentNegotiationConfigurer configurer) {
        configurer.ignoreAcceptHeader(true);
        configurer.favorPathExtension(false);
        configurer.favorParameter(false);
        configurer.defaultContentType(MediaType.ALL);
        super.configureContentNegotiation(configurer);
    }

    /**
     * 解决跨域问题
     * @return
     */
    @Bean
    public FilterRegistrationBean corsFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean(
                new CorsFilter());
        return registration;
    }

    /**
     * cookie 配置
     * @param cookieEnums
     * @return
     */
    private CookieConfiguration builderConfiguration(CookieEnums cookieEnums) {
        CookieConfiguration configuration = new CookieConfiguration();
        configuration.setName(cookieEnums.getName());
        configuration.setClientName(cookieEnums.getClientName());
        configuration.setCrypto(new AESCrypto());

        return configuration;
    }

    @Bean
    public CookyjarConfiguration cookyjarConfiguration() {
        CookyjarConfiguration cookyjarConfiguration = new CookyjarConfiguration();

        List<CookieConfiguration> cookieConfigurations = new ArrayList<>();

        cookieConfigurations.add(builderConfiguration(CookieEnums.PLT_USER));

        cookyjarConfiguration.setConfigurations(cookieConfigurations);
        return cookyjarConfiguration;
    }

    @Bean
    public FilterRegistrationBean cookyjarFilter() {
        CookyjarFilter cookyjarFilter = new CookyjarFilter();

        cookyjarFilter.setCookyjarConfiguration(cookyjarConfiguration());

        FilterRegistrationBean registration = new FilterRegistrationBean(
                cookyjarFilter);
        return registration;
    }

    /**
     * 过滤器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

        super.addArgumentResolvers(argumentResolvers);
    }

}
