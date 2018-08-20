package io.eleva.apigateway.gateway.mvc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

public class GatewayReturnValueHandler implements 
	HandlerMethodReturnValueHandler, InitializingBean{
	protected final Log logger = LogFactory.getLog(getClass());
	private GatewayMethodProcessor myMethodProcessor;
	
	public GatewayReturnValueHandler() {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>(1);
		messageConverters.add(new MappingJackson2HttpMessageConverter());
		myMethodProcessor = new GatewayMethodProcessor(messageConverters);
	}
	
	private static final boolean jackson2Present =
			ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper",
					GatewayArgumentResolver.class.getClassLoader()) &&
			ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator",
					GatewayArgumentResolver.class.getClassLoader());
	
	public void afterPropertiesSet() throws Exception {		
		
	}
	

	public boolean supportsReturnType(MethodParameter returnType) {
		return myMethodProcessor.supportsReturnType(returnType);
	}


	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
		/*if(returnValue!=null) {			
			GenericResponse response = (GenericResponse)returnValue;
			
			if(response.getData()!=null)  {
				Class clazz = response.getData().getClass();
				boolean isanno = clazz.isAnnotationPresent(apioauthresponse.class);
				if(isanno) {					
					HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
					request.setAttribute("gateway-oauth-response", returnValue);
					HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
					boolean isAuthcHandler = (Boolean)request.getAttribute("isGatewayAuthenticationHandler");
					
					if(isAuthcHandler) {
						OauthData data = (OauthData)response.getData();	
						Oauth2Token token = new Oauth2Token(data.getUserid());
						Subject subject = SecurityUtils.getSubject();		
						subject.login(token);
					}
					
				}
				
			}
			logger.info("response:"+(response.toString()));
		}
		*/
		myMethodProcessor.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
		
	}

}
