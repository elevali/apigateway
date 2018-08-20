package io.eleva.apigateway.gateway.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import io.eleva.apigateway.api.annotation.apiservice;
import io.eleva.apigateway.gateway.spi.ServiceBootstrap;

public class GatewayHandlerMappingRegistory implements ApplicationContextAware,InitializingBean{
	private RequestMappingHandlerMapping requestMappingHandlerMapping;
	
	public GatewayHandlerMappingRegistory(RequestMappingHandlerMapping requestMappingHandlerMapping) {
		this.requestMappingHandlerMapping = requestMappingHandlerMapping;
	}
	private ApplicationContext context;
	public void init() {
		RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();
		config.setUrlPathHelper(requestMappingHandlerMapping.getUrlPathHelper());
		config.setPathMatcher(requestMappingHandlerMapping.getPathMatcher());
		config.setSuffixPatternMatch(requestMappingHandlerMapping.useSuffixPatternMatch());
		config.setTrailingSlashMatch(requestMappingHandlerMapping.useTrailingSlashMatch());
		config.setRegisteredSuffixPatternMatch(requestMappingHandlerMapping.useRegisteredSuffixPatternMatch());
		config.setContentNegotiationManager(requestMappingHandlerMapping.getContentNegotiationManager());
		
		Map<String,Class<?>> services = ServiceBootstrap.getInstance().loadServiceClasses();
		for(String key:services.keySet()) {				
			Object bean = pickBean(context,services.get(key));
			apiservice anno = AnnotationUtils.findAnnotation(bean.getClass(),apiservice.class);
			if(anno!=null) {
				Class<?> clazz =services.get(key);
				Method[] methods = clazz.getMethods();
				for(Method m : methods) {							
					String urlpath ="/";
					if(anno.prefix()!=null && !anno.prefix().isEmpty()) {
						urlpath += anno.prefix();	
					}
					urlpath = urlpath+"/"+anno.value().toLowerCase()+"/"+m.getName().toLowerCase();
					RequestMethod[] requestMethods = new RequestMethod[1];
					requestMethods[0] = RequestMethod.POST;
					RequestMappingInfo.Builder builder = RequestMappingInfo
							.paths(urlpath)
							.methods(requestMethods)
							.mappingName(anno.value()+m.getName());
					requestMappingHandlerMapping.registerMapping(builder.options(config).build(), bean, m);
				}
			}
		}
		
		
		/*
		try {
			for(String beanname:beannames) {				
				Class clazz = AopTargetUtils.getTarget(context.getBean(beanname)).getClass();
				Class[] interfaceClazzs = clazz.getInterfaces();
				for(Class interfaceClazz:interfaceClazzs) {
					if(interfaceClazz.isAnnotationPresent(apiservice.class)) {
						apiInfos.put(beanname, interfaceClazz);
						break;
					}				
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		for(String apibean:apiInfos.keySet()) {
			Object handler = context.getBean(apibean);		
			Class clazz = (Class)apiInfos.get(apibean);
			apiservice anno = (apiservice)clazz.getAnnotation(apiservice.class);
			Method[] methods = clazz.getMethods();
			for(Method m:methods) {
				String urlpath ="/";
				if(anno.prefix()!=null && !anno.prefix().isEmpty()) {
					urlpath += anno.prefix();	
				}
				urlpath = urlpath+"/"+anno.value()+"/"+m.getName();
				RequestMethod[] requestMethods = new RequestMethod[1];
				requestMethods[0] = RequestMethod.POST;
				RequestMappingInfo.Builder builder = RequestMappingInfo
						.paths(urlpath)
						.methods(requestMethods)
						.mappingName(anno.value()+m.getName());
				requestMappingHandlerMapping.registerMapping(builder.options(config).build(), handler, m);
			}
			
		}*/
		
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}
	
	public Annotation getAnnotation(Method m,Class targetClass,Class<? extends Annotation> clazz) {
		 Annotation a = AnnotationUtils.findAnnotation(m, clazz);
	     if (a != null) return a;

        //The MethodInvocation's method object could be a method defined in an interface.
        //However, if the annotation existed in the interface's implementation (and not
        //the interface itself), it won't be on the above method object.  Instead, we need to
        //acquire the method representation from the targetClass and check directly on the
        //implementation itself:    
      
        m = ClassUtils.getMostSpecificMethod(m, targetClass);
        a = AnnotationUtils.findAnnotation(m, clazz);
        if (a != null) return a;
        
        return null;
	}
	
	private Object pickBean(ApplicationContext context,Class clazz) {
		Map<String, ?> beans= context.getBeansOfType(clazz);
		Iterator<String> iterator = beans.keySet().iterator();
		Object cglib = null;
		Object aop = null;
		Object bean = null;
		while(iterator.hasNext()) {
			String key = iterator.next();
			bean = beans.get(key);
			if(AopUtils.isCglibProxy(bean)) {
				cglib = bean;
			}
			if(cglib==null && AopUtils.isAopProxy(bean)) {
				aop = bean;
			}
		}
		if(cglib!=null) {
			return cglib;
		}else if(aop!=null) {
			return aop;
		}else {
			return bean;
		}
	}
	

}
