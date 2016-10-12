package com.ylyao.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by yaoxun on 2016/10/12.
 */
@Configuration
@EnableCaching
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan(basePackages = {"com.ylyao.template"})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        Class[] cls = {Application.class};
        SpringApplication.run(cls, args);
    }

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

}
