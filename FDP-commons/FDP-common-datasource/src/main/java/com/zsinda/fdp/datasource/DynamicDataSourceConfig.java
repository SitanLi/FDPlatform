package com.zsinda.fdp.datasource;


import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zsinda.fdp.dds.DynamicDataSource;
import com.zsinda.fdp.properties.DataSourceCoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;



/**
 * @program: FDPlatform
 * @description: 多数据源配置
 * @author: Sinda
 * @create: 2020-01-29 15:30
 */
@Slf4j
public class DynamicDataSourceConfig extends BaseDataSourceConfig {

    private final DataSourceCoreProperties dataSourceCoreProperties;

    @Bean(name = "datasources")
    public Map<String, DataSource> datasources() {
        Map<String, DataSource> datasources = new LinkedHashMap();
        Map<String, DruidDataSource> dataSourcesPropertiesMap = dataSourceCoreProperties.getDatasources();
        for (Map.Entry<String, DruidDataSource> entry : dataSourcesPropertiesMap.entrySet()) {
            datasources.put(entry.getKey(), entry.getValue());
        }
        return datasources;
    }

    /**
     * 动态数据源配置
     */
    @Bean
    @Primary
    public DataSource dataSource(@Qualifier("datasources") Map<String, DataSource> datasources) {
        log.info("开始 -> 初始化动态数据源");
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap(datasources.size());
        for (Map.Entry<String, DataSource> entry : datasources.entrySet()) {
            targetDataSources.put(entry.getKey(), entry.getValue());
            log.info("初始化 -> {} 数据源",entry.getKey());
        }
        dynamicDataSource.setTargetDataSources(targetDataSources);
        //默认数据源配置 第一个
        dynamicDataSource.setDefaultTargetDataSource(datasources.get(datasources.keySet().iterator().next()));
        log.info("完毕 -> 初始化动态数据源,共计 {} 条", targetDataSources.size());
        return dynamicDataSource;
    }

    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws Exception {
        return super.sqlSessionFactory(dataSource);
    }

    public DynamicDataSourceConfig(DataSourceCoreProperties dataSourceCoreProperties, PaginationInterceptor paginationInterceptor) {
        super(dataSourceCoreProperties, paginationInterceptor);
        this.dataSourceCoreProperties=dataSourceCoreProperties;
    }
}
