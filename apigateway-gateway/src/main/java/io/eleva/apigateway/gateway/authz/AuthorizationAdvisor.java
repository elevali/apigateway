package io.eleva.apigateway.gateway.authz;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.shiro.mgt.SecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.core.annotation.AnnotationUtils;

import io.eleva.apigateway.api.annotation.apiservice;
import io.eleva.apigateway.api.annotation.authz.RequiresAuthentication;
import io.eleva.apigateway.api.annotation.authz.RequiresGuest;
import io.eleva.apigateway.api.annotation.authz.RequiresPermissions;
import io.eleva.apigateway.api.annotation.authz.RequiresRoles;
import io.eleva.apigateway.api.annotation.authz.RequiresUser;

public class AuthorizationAdvisor extends StaticMethodMatcherPointcutAdvisor {
	private static final Logger log = LoggerFactory.getLogger(AuthorizationAdvisor.class);
	private static final Class<? extends Annotation>[] AUTHZ_ANNOTATION_CLASSES =
            new Class[] {
                    RequiresPermissions.class, RequiresRoles.class,
                    RequiresUser.class, RequiresGuest.class, RequiresAuthentication.class
            };
	
	protected SecurityManager securityManager = null;
	public SecurityManager getSecurityManager() {
	     return securityManager;
	}

	public void setSecurityManager(org.apache.shiro.mgt.SecurityManager securityManager) {
	     this.securityManager = securityManager;
	}
	
	
	public AuthorizationAdvisor() {
		setAdvice(new GatewayAopAllianceAnnotationsAuthorizingMethodInterceptor());
	}

	@Override
	public boolean matches(Method method, Class targetClass) {
		Class targetInterfaceClazz = findApiInterface(targetClass);
		if(targetInterfaceClazz==null) {
			return false;
		}
		
		Method m = null;
		try {
			m = targetInterfaceClazz.getMethod(method.getName(), method.getParameterTypes());
		}catch(Exception e) {
			//log.error(method.getName(), e);
			return false;
		}
		
		if ( isAuthzAnnotationPresent(m) || isAuthzAnnotationPresent(targetClass)) {
            return true;
        }
		
		return false;
	}
	private boolean isAuthzAnnotationPresent(Class<?> targetClazz) {		
        for( Class<? extends Annotation> annClass : AUTHZ_ANNOTATION_CLASSES ) {
            Annotation a = AnnotationUtils.findAnnotation(targetClazz, annClass);
            if ( a != null ) {
                return true;
            }
        }
        return false;
    }

    private boolean isAuthzAnnotationPresent(Method method) {
        for( Class<? extends Annotation> annClass : AUTHZ_ANNOTATION_CLASSES ) {
            Annotation a = AnnotationUtils.findAnnotation(method, annClass);
            if ( a != null ) {
                return true;
            }
        }
        return false;
    }
    
    private Class findApiInterface(Class targetClazz) {
    	if(targetClazz.isInterface()) {
    		targetClazz.isAnnotationPresent(apiservice.class);
    		return targetClazz;
    	}
    	
    	Class[] interfacesClazz = targetClazz.getInterfaces();
		for(Class interfaceClazz : interfacesClazz) {
			if(interfaceClazz.isAnnotationPresent(apiservice.class)) {
				return interfaceClazz;
			}
				
		}
		return null;
    }
   
}
