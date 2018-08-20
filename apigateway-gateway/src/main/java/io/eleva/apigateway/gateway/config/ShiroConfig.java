package io.eleva.apigateway.gateway.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.eleva.apigateway.gateway.GatewayPropertyBean;
import io.eleva.apigateway.gateway.authc.Oauth2Filter;
import io.eleva.apigateway.gateway.authc.Oauth2Realm;
import io.eleva.apigateway.gateway.authz.AuthorizationAdvisor;

@Configuration
public class ShiroConfig {

	@Bean
	@ConfigurationProperties(prefix = "gateway")
	public GatewayPropertyBean gatewayPropertyBean() {
		GatewayPropertyBean gatewayPropertyBean = new GatewayPropertyBean();
		return gatewayPropertyBean;
	}
	@Bean
	public EhCacheManager cacheManager() {
		EhCacheManager cacheManager = new EhCacheManager();
		cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
		return cacheManager;
	}
	@Bean
	public Oauth2Realm oauth2Realm() {
		Oauth2Realm realm = new Oauth2Realm();
		realm.setCacheManager(cacheManager());	
		realm.setCredentialsMatcher(new AllowAllCredentialsMatcher());
		realm.setCachingEnabled(true);
		realm.setAuthenticationCachingEnabled(false);
		realm.setAuthorizationCachingEnabled(true);
		realm.setAuthorizationCacheName("authorizationCache");
		realm.setName("oauth2realm");
		return realm;
	}
	
	@Bean("sessionManager")
    public SessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setGlobalSessionTimeout(1800000);
        sessionManager.setDeleteInvalidSessions(true);
        return sessionManager;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(Oauth2Realm cookieAuthRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(cookieAuthRealm);
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(cacheManager());
        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        
        Map<String, Filter> filters = new HashMap<>();
        filters.put("oauth2", new Oauth2Filter());
        shiroFilter.setFilters(filters);

        Map<String, String> filterMap = new LinkedHashMap<>(); 
        GatewayPropertyBean gatewayPropertyBean = gatewayPropertyBean();
        String loginUrls = gatewayPropertyBean.getLoginUrls();
        if(loginUrls!=null && !loginUrls.isEmpty()) {
        	String[] loginUrlsArray = loginUrls.split(",");
        	for(String url:loginUrlsArray) {
        		if(!url.trim().isEmpty()) {
        			filterMap.put(url, "anon");
        		}        		
        	}
        }     
        filterMap.put("/captcha.jpg", "anon");
        filterMap.put("/**", "oauth2");
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAdvisor authorizationAdvisor(SecurityManager securityManager) {
    	AuthorizationAdvisor advisor = new AuthorizationAdvisor();
        advisor.setSecurityManager(securityManager);
        advisor.setOrder(Integer.MAX_VALUE-2);
        return advisor;
    }

}
