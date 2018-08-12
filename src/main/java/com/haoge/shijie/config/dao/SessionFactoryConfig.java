package com.haoge.shijie.config.dao;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration

public class SessionFactoryConfig {

    //mybatis mapper文件的路径
    @Value("${mapper_path}")
    private String mapperPath;

    //mybatis-config.xml配置文件的路径
    @Value("${mybatis_config_file}")
    private String mybatisConfigFilePath;

    @Autowired
    private DataSource dataSource;

    //实体类所在的package
    @Value("${entity_package}")
    private String entityPackage;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean creatSqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
        ssfb.setConfigLocation(new ClassPathResource(mybatisConfigFilePath));
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String packageSearchPath = PathMatchingResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + mapperPath;
        ssfb.setMapperLocations(resolver.getResources(packageSearchPath));
        ssfb.setDataSource(dataSource);
        ssfb.setTypeAliasesPackage(entityPackage);
        return ssfb;
    }
}