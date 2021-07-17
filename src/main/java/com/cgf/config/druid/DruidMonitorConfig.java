package com.cgf.config.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Druid配置
 */
@Configuration
public class DruidMonitorConfig {

    /*
       将自定义的 Druid数据源添加到容器中，不再让 Spring Boot 自动创建
       绑定全局配置文件中的 druid 数据源属性到 com.alibaba.druid.pool.DruidDataSource从而让它们生效
       @ConfigurationProperties(prefix = "spring.datasource")：作用就是将 全局配置文件中
       前缀为 spring.datasource的属性值注入到 com.alibaba.druid.pool.DruidDataSource 的同名参数中
     */
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }

    //配置Druid监控管理后台的servlet
    //内置Servlet容器时没有web.xml文件，所以使用springboot的注册servlet的方式
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        //这些参数可以在com.alibaba.druid.support.http.StatViewServlet的父类com.alibaba.druid.support.http.ResourceServlet 中找到
        Map<String,String> initParams = new HashMap<>();
//        initParams.put("loginUsername","admin");//后台管理界面的登录账号
//        initParams.put("loginPassword","123456");//后台管理界面的登录密码
        //后台允许谁可以访问
        //initParams.put("allow","localhost");//表示只有本机可以访问
        initParams.put("allow","");//为空或者为null时，表示允许所有访问
        //deny:Druid后台拒绝访问谁
//        initParams.put("dz","192.168.1.20");//表示禁止此ip访问
        //设置初始化参数
        bean.setInitParameters(initParams);
        return bean;
    }
}
