package io.eleva.apigateway.gateway.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import io.eleva.apigateway.gateway.GatewayPropertyBean;
import io.eleva.apigateway.gateway.mvc.GatewayArgumentResolver;
import io.eleva.apigateway.gateway.mvc.GatewayHandlerMappingRegistory;
import io.eleva.apigateway.gateway.mvc.GatewayReturnValueHandler;
import io.eleva.apigateway.gateway.mvc.Oauth2ReturnValueHandler;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport{
	@Autowired
	GatewayPropertyBean gatewayPropertyBean;	
	
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
	    super.addArgumentResolvers(argumentResolvers);
	    argumentResolvers.add(new GatewayArgumentResolver());
	}
	
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
	    super.addReturnValueHandlers(returnValueHandlers);
	    returnValueHandlers.add(new GatewayReturnValueHandler());
	    returnValueHandlers.add(new Oauth2ReturnValueHandler());
	}
/*	
	@Bean
	public GatewayOauth2Interceptor oauth2Interceptor() {
		GatewayOauth2Interceptor interceptor = new GatewayOauth2Interceptor();
		return interceptor;
	}
	
	public void addInterceptors(InterceptorRegistry registry) {
		super.addInterceptors(registry);
		String loginUrls = gatewayPropertyBean.getLoginUrls();
		registry.addInterceptor(oauth2Interceptor()).addPathPatterns(loginUrls.split(","));
	}*/
	@Bean
	public GatewayHandlerMappingRegistory handlerMappingConfig() {
		GatewayHandlerMappingRegistory handlerMappingConfig = 
				new GatewayHandlerMappingRegistory(requestMappingHandlerMapping());
		return handlerMappingConfig;
	}
}
