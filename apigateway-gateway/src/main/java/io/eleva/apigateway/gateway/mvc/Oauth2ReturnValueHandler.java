package io.eleva.apigateway.gateway.mvc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import io.eleva.apigateway.api.dto.Oauth2Response;
import io.eleva.apigateway.gateway.authc.Oauth2Token;

public class Oauth2ReturnValueHandler implements HandlerMethodReturnValueHandler{
	protected final Log logger = LogFactory.getLog(getClass());
	private GatewayMethodProcessor myMethodProcessor;

	public Oauth2ReturnValueHandler() {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>(1);
		messageConverters.add(new MappingJackson2HttpMessageConverter());
		myMethodProcessor = new GatewayMethodProcessor(messageConverters);
	}
	private static final boolean jackson2Present =
			ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper",
					GatewayArgumentResolver.class.getClassLoader()) &&
			ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator",
					GatewayArgumentResolver.class.getClassLoader());
	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		if(returnType.getParameterType().isAssignableFrom(Oauth2Response.class)) {
			return true;
		}		
		return false;
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
		Oauth2Response response = (Oauth2Response)returnValue;
		if(response.getResult()==Oauth2Response.SUCCESS 
				&& (response.getUserid()!=null)&& !response.getUserid().isEmpty() ){
			Oauth2Token token = new Oauth2Token(response.getUserid());
			Subject subject = SecurityUtils.getSubject();		
			subject.login(token);
		}	
		myMethodProcessor.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
	}

}
