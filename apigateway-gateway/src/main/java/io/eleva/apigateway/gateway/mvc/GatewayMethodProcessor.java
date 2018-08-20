package io.eleva.apigateway.gateway.mvc;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DelegatingSubject;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;

import io.eleva.apigateway.api.dto.GenericRequest;
import io.eleva.apigateway.api.dto.GenericResponse;

public class GatewayMethodProcessor extends AbstractMessageConverterMethodProcessor{

	protected final Log logger = LogFactory.getLog(getClass());
	
	public GatewayMethodProcessor(List<HttpMessageConverter<?>> converters) {
		super(converters);
		// TODO Auto-generated constructor stub
	}
	
	public GatewayMethodProcessor(List<HttpMessageConverter<?>> converters,
			ContentNegotiationManager manager) {
		super(converters, manager);
	}
	
	public GatewayMethodProcessor(List<HttpMessageConverter<?>> converters,
			List<Object> requestResponseBodyAdvice) {
		super(converters, null, requestResponseBodyAdvice);
	}
	
	public GatewayMethodProcessor(List<HttpMessageConverter<?>> converters,
			ContentNegotiationManager manager, List<Object> requestResponseBodyAdvice) {
		super(converters, manager, requestResponseBodyAdvice);
	}

	
	public boolean supportsReturnType(MethodParameter returnType) {
		if(returnType.getParameterType().isAssignableFrom(GenericResponse.class)) {
			return true;
		}		
		return false;
	}

	
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
		mavContainer.setRequestHandled(true);
		ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
		ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
		// Try even with null return value. ResponseBodyAdvice could get involved.
		writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
		
	}

	
	public boolean supportsParameter(MethodParameter parameter) {
		Class clazz = parameter.getParameterType();
		if(clazz.isAssignableFrom(GenericRequest.class)) {
			return true;
		}
			
		return false;
	}

	
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		parameter = parameter.nestedIfOptional();
		Object arg = readWithMessageConverters(webRequest, parameter, parameter.getNestedGenericParameterType());
		return adaptArgumentIfNecessary(arg, parameter);
	}
	
	protected <T> Object readWithMessageConverters(NativeWebRequest webRequest, MethodParameter parameter,
			Type paramType) throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {

		HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(servletRequest);

		Object arg = readWithMessageConverters(inputMessage, parameter, paramType);
		if(arg == null) {
			return arg;
		}
		GenericRequest req = (GenericRequest)arg;
		Subject subject = SecurityUtils.getSubject();  
		if(subject.isAuthenticated()) {			
			req.setUserid((String)subject.getPrincipal());
			req.setReqaddr(((DelegatingSubject)subject).getHost());
		}
		logger.info("request:"+req.toString());
		return arg;
	}

}
