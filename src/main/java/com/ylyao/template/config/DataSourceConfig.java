package com.ylyao.template.config;

import com.github.pagehelper.PageHelper;
import com.ylyao.template.mybatis.*;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by yaoxun on 2015/10/28.
 */
@Configuration
@MapperScan(basePackages = {"com.ylyao.template.mapper"})
public class DataSourceConfig {

    @Resource
    private DataSource dataSource;

    private String typeAliasesPackage = "com.ylyao.template.domain";

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setTypeAliasesPackage(typeAliasesPackage);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver
                .getResources("classpath:/mapper/*.xml"));
        sessionFactory.setTypeHandlers(new TypeHandler[]{
                new LocalDateTimeHandler(),
                new LocalDateHandler()});
        sessionFactory.setPlugins(new Interceptor[]{new PageHelper()});

        return sessionFactory.getObject();
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }


}
