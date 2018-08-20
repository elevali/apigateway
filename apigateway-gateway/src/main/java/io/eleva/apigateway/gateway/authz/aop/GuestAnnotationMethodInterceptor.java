package io.eleva.apigateway.gateway.authz.aop;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;

public class GuestAnnotationMethodInterceptor extends AuthorizingAnnotationMethodInterceptor {

    /**
     * Default no-argument constructor that ensures this interceptor looks for
     * {@link org.apache.shiro.authz.annotation.RequiresGuest RequiresGuest} annotations in a method
     * declaration.
     */
    public GuestAnnotationMethodInterceptor() {
        super(new GuestAnnotationHandler());
    }

    /**
     * @param resolver
     * @since 1.1
     */
    public GuestAnnotationMethodInterceptor(AnnotationResolver resolver) {
        super(new GuestAnnotationHandler(), resolver);
    }

}
