package com.blog.config;

import com.blog.Realm.ShiroRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;


@Configuration
public class ShiroSpringConfig {
	@Bean
	public ShiroFilterFactoryBean shirFilter(DefaultWebSecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		filterChainDefinitionMap.put("/static/**", "anon");	// 静态资源匿名访问
		filterChainDefinitionMap.put("/templates/**", "anon");	// 静态资源匿名访问
		filterChainDefinitionMap.put("/login", "anon");// 登录匿名访问
		filterChainDefinitionMap.put("/login/**", "anon");// 登录匿名访问
		filterChainDefinitionMap.put("/register", "anon");// 注册匿名访问
		filterChainDefinitionMap.put("/register/**", "anon");// 注册匿名访问
		filterChainDefinitionMap.put("/question", "anon");// 问题查看匿名访问
		filterChainDefinitionMap.put("/logout", "logout");	// 用户退出
		filterChainDefinitionMap.put("/**", "authc");		// 其他路径均需要身份认证，一般位于最下面，优先级最低
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		shiroFilterFactoryBean.setLoginUrl("/index");		// 登录的路径
		shiroFilterFactoryBean.setSuccessUrl("/index");	// 登录成功后跳转的路径
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");	// 验证失败后跳转的路径
		return shiroFilterFactoryBean;
	}
	
	//配置Shiro生命周期处理器
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }
    
    //自动创建代理类，若不添加，Shiro的注解可能不会生效。
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
    
    //开启Shiro的注解
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }
	
    //配置加密匹配，使用MD5的方式，进行1次加密
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName("MD5");
		hashedCredentialsMatcher.setHashIterations(1);
		return hashedCredentialsMatcher;
	}

	//自定义Realm，可以多个
	@Bean
	public ShiroRealm ShiroRealm() {
		ShiroRealm ShiroRealm = new ShiroRealm();
		ShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		return ShiroRealm;
	}

	//SecurityManager 安全管理器；Shiro的核心
	@Bean
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(ShiroRealm());
		return securityManager;
	}

}
