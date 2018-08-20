package io.eleva.apigateway.gateway.mvc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class GatewayArgumentResolver implements 
	HandlerMethodArgumentResolver, InitializingBean{
	
	private GatewayMethodProcessor myMethodProcessor;
	
	public GatewayArgumentResolver() {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>(1);
		messageConverters.add(new MappingJackson2HttpMessageConverter());
		myMethodProcessor = new GatewayMethodProcessor(messageConverters);
	}
	
	private static final boolean jackson2Present =
			ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper",
					GatewayArgumentResolver.class.getClassLoader()) &&
			ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator",
					GatewayArgumentResolver.class.getClassLoader());
	
	public boolean supportsParameter(MethodParameter parameter) {
		return myMethodProcessor.supportsParameter(parameter);
	}
	
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		return myMethodProcessor.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
	}

	
	public void afterPropertiesSet() throws Exception {		

	}

}
