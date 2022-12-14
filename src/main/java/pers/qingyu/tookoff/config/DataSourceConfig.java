package pers.qingyu.tookoff.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Base64;


/**
 * 数据库配置
 */
@Configuration
@EnableConfigurationProperties(LiquibaseProperties.class)
public class DataSourceConfig {

    @Bean
    public DataSource getDataSource(DataSourceProperties dataSourceProperties) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(dataSourceProperties.getUrl());
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(new String(Base64.getDecoder().decode(dataSourceProperties.getPassword())));
        return dataSource;
    }
}
