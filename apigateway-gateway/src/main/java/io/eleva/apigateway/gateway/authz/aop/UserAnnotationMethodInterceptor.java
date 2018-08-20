package io.eleva.apigateway.gateway.authz.aop;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;

public class UserAnnotationMethodInterceptor extends AuthorizingAnnotationMethodInterceptor {

    /**
     * Default no-argument constructor that ensures this interceptor looks for
     *
     * {@link org.apache.shiro.authz.annotation.RequiresUser RequiresUser} annotations in a method
     * declaration.
     */
    public UserAnnotationMethodInterceptor() {
        super( new UserAnnotationHandler() );
    }

    /**
     *
     * @param resolver
     * @since 1.1
     */
    public UserAnnotationMethodInterceptor(AnnotationResolver resolver) {
        super(new UserAnnotationHandler(), resolver);
    }

}
