package io.eleva.apigateway.gateway.authz.aop;

import java.lang.annotation.Annotation;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;

import io.eleva.apigateway.api.annotation.authz.RequiresUser;


/**
 * Checks to see if a @{@link org.apache.shiro.authz.annotation.RequiresUser RequiresUser} annotation
 * is declared, and if so, ensures the calling <code>Subject</code> is <em>either</em>
 * {@link org.apache.shiro.subject.Subject#isAuthenticated() authenticated} <b><em>or</em></b> remembered via remember
 * me services before allowing access.
 * <p>
 * This annotation essentially ensures that <code>subject.{@link org.apache.shiro.subject.Subject#getPrincipal() getPrincipal()} != null</code>.
 *
 * @since 0.9.0
 */
public class UserAnnotationHandler extends AuthorizingAnnotationHandler {

    /**
     * Default no-argument constructor that ensures this handler looks for
     *
     * {@link org.apache.shiro.authz.annotation.RequiresUser RequiresUser} annotations.
     */
    public UserAnnotationHandler() {
        super(RequiresUser.class);
    }

    /**
     * Ensures that the calling <code>Subject</code> is a <em>user</em>, that is, they are <em>either</code>
     * {@link org.apache.shiro.subject.Subject#isAuthenticated() authenticated} <b><em>or</em></b> remembered via remember
     * me services before allowing access, and if not, throws an
     * <code>AuthorizingException</code> indicating access is not allowed.
     *
     * @param a the RequiresUser annotation to check
     * @throws org.apache.shiro.authz.AuthorizationException
     *         if the calling <code>Subject</code> is not authenticated or remembered via rememberMe services.
     */
    public void assertAuthorized(Annotation a) throws AuthorizationException {
        if (a instanceof RequiresUser && getSubject().getPrincipal() == null) {
            throw new UnauthenticatedException("Attempting to perform a user-only operation.  The current Subject is " +
                    "not a user (they haven't been authenticated or remembered from a previous login).  " +
                    "Access denied.");
        }
    }
}
