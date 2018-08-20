package io.eleva.apigateway.gateway.authz.aop;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;

public class AuthenticatedAnnotationMethodInterceptor extends AuthorizingAnnotationMethodInterceptor {

    /**
     * Default no-argument constructor that ensures this interceptor looks for
     * {@link org.apache.shiro.authz.annotation.RequiresAuthentication RequiresAuthentication} annotations in a method
     * declaration.
     */
    public AuthenticatedAnnotationMethodInterceptor() {
        super(new AuthenticatedAnnotationHandler());
    }

    /**
     * @param resolver
     * @since 1.1
     */
    public AuthenticatedAnnotationMethodInterceptor(AnnotationResolver resolver) {
        super(new AuthenticatedAnnotationHandler(), resolver);
    }
}
