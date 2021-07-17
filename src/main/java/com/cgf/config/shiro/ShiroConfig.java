package com.cgf.config.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.LinkedHashMap;
import java.util.Map;

/***
 * @author cgf
 * @Description shiro与redis配置类
 * @date 2021/4/29
 */
@Configuration
public class ShiroConfig {
    //最大连接数
    @Value("${redis.pool.maxTotal:100}")
    private Integer maxTotal;

    //最大空闲连接数
    @Value("${redis.pool.maxIdle:5}")
    private Integer maxIdle;

    //最小空闲连接数
    @Value("${redis.pool.minIdle:5}")
    private Integer minIdle;

    @Value("${redis.shard.timeout:5000}")
    private Integer timeout;

    @Value("${redis.host}")
    private String host;

    @Value("${redis.password}")
    private String password;

    @Value("${redis.pool.maxWaitMillis:60000}")
    private Integer maxWaitMillis;

    @Value("${redis.port}")
    private Integer port;
    /**
     * @Author cgf
     * @Description //TODO 配置shiro过滤
     * @Date 21:35 2021/5/9
     * @Param [securityManager]
     **/
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        /*
         * anon:所有url都都可以匿名访问，authc:所有url都必须认证通过才可以访问;
         * 过滤链定义，从上向下顺序执行，authc 应放在 anon 下面
         * */
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 配置不会被拦截的链接 顺序判断，因为前端模板采用了thymeleaf，这里不能直接使用 ("/static/**", "anon")来配置匿名访问，必须配置到每个静态目录
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/html/**", "anon");


        //swagger接口权限 开放
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");

        filterChainDefinitionMap.put("*.html", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/captcha.jpg", "anon");

        filterChainDefinitionMap.put("/auth/**","anon");
        // 所有url都必须认证通过才可以访问
        //filterChainDefinitionMap.put("/**", "authc");
        
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * @Author cgf
     * @Description //TODO 权限管理，配置主要是Realm的管理认证
     * @Date 21:17 2021/5/9
     * @Param []
     **/
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        securityManager.setRealm(myShiroRealm());
        return securityManager;
    }

    /**
     * @Author cgf
     * @Description //TODO 凭证匹配器（密码校验交给Shiro的SimpleAuthenticationInfo进行处理)
     * @Date 21:18 2021/5/9
     * @Param []
     **/
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        // 散列的次数，比如散列两次，相当于 md5(md5(""));
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }
    

    /**
     * @Author cgf
     * @Description //TODO 自定义身份认证realm,将自己的验证方式加入容器
     * @Date 21:20 2021/5/9
     * @Param []
     **/
    @Bean
    public MyShiroRealm myShiroRealm(){
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        //myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    /**
     * @Author cgf
     * @Description //TODO redis缓存实现
     * @Date 21:36 2021/5/9
     * @Param []
     **/
    @Bean
    public RedisCacheManager cacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        // 必须要设置主键名称，shiro-redis 插件用过这个缓存用户信息
        redisCacheManager.setPrincipalIdFieldName("userId");
        return redisCacheManager;
    }

    /**
     * @Author cgf
     * @Description //TODO 配置redisManager
     * @Date 21:36 2021/5/9
     * @Param []
     **/
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        System.out.println(minIdle + " " + maxIdle + " " + maxWaitMillis + " " +maxTotal);
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);
        redisManager.setJedisPool(jedisPool);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(password)) {
            redisManager.setPassword(password);
        }
        //        System.out.println(env);
        return redisManager;
    }

    /**
     * @Author cgf
     * @Description //TODO 配置sessionManager
     * @Date 21:36 2021/5/9
     * @Param []
     **/
    @Bean
    public DefaultWebSessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new MySessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        //禁用cookie
        sessionManager.setSessionIdCookieEnabled(false);
        //30分鐘后清除
        sessionManager.setGlobalSessionTimeout(30*60*1000);
        return sessionManager;
    }

    /**
     * @Author cgf
     * @Description //TODO 通过redis对sessionDao层的实现
     * @Date 21:36 2021/5/9
     * @Param []
     **/
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        redisSessionDAO.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
        //redisSessionDAO.setKeyPrefix("shiro:session");
        redisSessionDAO.setExpire(30*60*1000); //30分鐘
        return redisSessionDAO;
    }

    /**
     * @Author cgf
     * @Description //TODO 授权所用配置
     * @Date 21:36 2021/5/9
     * @Param []
     **/
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    /**
     * @Author cgf
     * @Description //TODO securityManager配置
     * @Date 21:36 2021/5/9
     **/
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }


    /**
     * @Author cgf
     * @Description //TODO 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * @Date 21:36 2021/5/9
     **/
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * @Author cgf
     * @Description //TODO Shiro生命周期处理器
     * @Date 21:36 2021/5/9
     **/
    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * @Author cgf
     * @Description //TODO cookie配置
     * @Date 21:36 2021/5/9
     **/
    //@Bean
    //public SimpleCookie cookie() {
    //    // cookie的name,对应的默认是 JSESSIONID
    //    SimpleCookie cookie = new SimpleCookie("SHARE_JSESSIONID");
    //    cookie.setHttpOnly(true);
    //    //  path为 / 用于多个系统共享 JSESSIONID
    //    cookie.setPath("/");
    //    return cookie;
    //}

}
