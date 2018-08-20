package io.eleva.apigateway.gateway.authz;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;
import org.apache.shiro.spring.aop.SpringAnnotationResolver;
import org.apache.shiro.spring.security.interceptor.AopAllianceAnnotationsAuthorizingMethodInterceptor;

import io.eleva.apigateway.gateway.authz.aop.AuthenticatedAnnotationMethodInterceptor;
import io.eleva.apigateway.gateway.authz.aop.GuestAnnotationMethodInterceptor;
import io.eleva.apigateway.gateway.authz.aop.PermissionAnnotationMethodInterceptor;
import io.eleva.apigateway.gateway.authz.aop.RoleAnnotationMethodInterceptor;
import io.eleva.apigateway.gateway.authz.aop.UserAnnotationMethodInterceptor;

public class GatewayAopAllianceAnnotationsAuthorizingMethodInterceptor extends AopAllianceAnnotationsAuthorizingMethodInterceptor{
	public GatewayAopAllianceAnnotationsAuthorizingMethodInterceptor() {
		 List<AuthorizingAnnotationMethodInterceptor> interceptors =
	                new ArrayList<AuthorizingAnnotationMethodInterceptor>(5);

	        //use a Spring-specific Annotation resolver - Spring's AnnotationUtils is nicer than the
	        //raw JDK resolution process.
	        AnnotationResolver resolver = new SpringAnnotationResolver();
	        //we can re-use the same resolver instance - it does not retain state:
	        interceptors.add(new RoleAnnotationMethodInterceptor(resolver));
	        interceptors.add(new PermissionAnnotationMethodInterceptor(resolver));
	        interceptors.add(new AuthenticatedAnnotationMethodInterceptor(resolver));
	        interceptors.add(new UserAnnotationMethodInterceptor(resolver));
	        interceptors.add(new GuestAnnotationMethodInterceptor(resolver));

	        setMethodInterceptors(interceptors);
	}
}
