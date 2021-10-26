package com.xiyoulinux.activity.comment.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import io.seata.rm.datasource.DataSourceProxy;
import io.seata.spring.annotation.GlobalTransactionScanner;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;


/**
 * seata 所需要的数据源代理配置类
 *
 * @author qkm
 */

@Configuration
public class DataSourceProxyAutoConfiguration {

    private final DataSourceProperties dataSourceProperties;

    @Autowired
    public DataSourceProxyAutoConfiguration(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    /**
     * init durid datasource
     *
     * @return druidDataSource  datasource instance
     */
    @Bean
    @Primary
    public DruidDataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(dataSourceProperties.getUrl());
        druidDataSource.setUsername(dataSourceProperties.getUsername());
        druidDataSource.setPassword(dataSourceProperties.getPassword());
        druidDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        druidDataSource.setInitialSize(0);
        druidDataSource.setMaxActive(180);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setMinIdle(0);
        druidDataSource.setValidationQuery("Select 1 from DUAL");
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(25200000);
        druidDataSource.setRemoveAbandoned(true);
        druidDataSource.setRemoveAbandonedTimeout(1800);
        druidDataSource.setLogAbandoned(true);
        return druidDataSource;
    }

    /**
     * init datasource proxy
     *
     * @param druidDataSource datasource bean instance
     * @return DataSourceProxy  datasource proxy
     */
    @Bean
    public DataSourceProxy dataSourceProxy(DruidDataSource druidDataSource) {
        return new DataSourceProxy(druidDataSource);
    }

    /**
     * init mybatis sqlSessionFactory
     *
     * @param dataSourceProxy datasource proxy
     * @return DataSourceProxy  datasource proxy
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSourceProxy dataSourceProxy) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSourceProxy);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:/mapper/*Mapper.xml"));
        factoryBean.setTypeEnumsPackage("com.xiyoulinux.enums");
        factoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
        factoryBean.setPlugins(new PaginationInterceptor());
        return factoryBean.getObject();
    }

    /**
     * 本地事务起作用
     */
    @Bean("txManager")
    public PlatformTransactionManager txManager(DataSourceProxy dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 初始化TM(事务管理器).
     * 初始化RM(资源管理器).
     * 扫描Spring容器里所有的Bean,如果Bean上有指定的注解(@GlobalTransactional/@GlobalLock),则,对这个Bean进行AOP代理.
     *
     * @return GlobalTransactionScanner
     */
    @Bean
    public GlobalTransactionScanner globalTransactionScanner() {
        return new GlobalTransactionScanner("${spring.application.name}", "cs_xiyoulinux_test");
    }
}