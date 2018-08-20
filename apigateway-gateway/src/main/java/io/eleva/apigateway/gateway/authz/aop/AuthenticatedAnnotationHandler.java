package io.eleva.apigateway.gateway.authz.aop;

import java.lang.annotation.Annotation;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;

import io.eleva.apigateway.api.annotation.authz.RequiresAuthentication;

public class AuthenticatedAnnotationHandler extends AuthorizingAnnotationHandler {

    /**
     * Default no-argument constructor that ensures this handler to process
     * {@link org.apache.shiro.authz.annotation.RequiresAuthentication RequiresAuthentication} annotations.
     */
    public AuthenticatedAnnotationHandler() {
        super(RequiresAuthentication.class);
    }

    /**
     * Ensures that the calling <code>Subject</code> is authenticated, and if not, throws an
     * {@link org.apache.shiro.authz.UnauthenticatedException UnauthenticatedException} indicating the method is not allowed to be executed.
     *
     * @param a the annotation to inspect
     * @throws org.apache.shiro.authz.UnauthenticatedException if the calling <code>Subject</code> has not yet
     * authenticated.
     */
    public void assertAuthorized(Annotation a) throws UnauthenticatedException {
        if (a instanceof RequiresAuthentication && !getSubject().isAuthenticated() ) {
            throw new UnauthenticatedException( "The current Subject is not authenticated.  Access denied." );
        }
    }
}
