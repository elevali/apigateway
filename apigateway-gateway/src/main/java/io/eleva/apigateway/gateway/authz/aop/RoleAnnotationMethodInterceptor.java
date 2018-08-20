package io.eleva.apigateway.gateway.authz.aop;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;

public class RoleAnnotationMethodInterceptor extends AuthorizingAnnotationMethodInterceptor {

    /**
     * Default no-argument constructor that ensures this interceptor looks for
     * {@link RequiresRoles RequiresRoles} annotations in a method declaration.
     */
    public RoleAnnotationMethodInterceptor() {
        super( new RoleAnnotationHandler() );
    }

    /**
     * @param resolver
     * @since 1.1
     */
    public RoleAnnotationMethodInterceptor(AnnotationResolver resolver) {
        super(new RoleAnnotationHandler(), resolver);
    }
}
