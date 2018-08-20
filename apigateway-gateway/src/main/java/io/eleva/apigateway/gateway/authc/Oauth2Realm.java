package io.eleva.apigateway.gateway.authc;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.Initializable;
import org.springframework.stereotype.Component;

@Component
public class Oauth2Realm extends AuthorizingRealm implements Initializable{
	
	
	private Class<? extends AuthenticationToken> authenticationTokenClass = Oauth2Token.class;
	public Oauth2Realm() {
		super();			
	}
	
	public Oauth2Realm(CacheManager cacheManager, CredentialsMatcher matcher) {
		super(cacheManager,matcher);
	}
	
	public void setName(String name) {
        super.setName(name);        
    }
	
	public boolean isAuthenticationCachingEnabled() {
		 return false;
	}
	
    public boolean supports(AuthenticationToken token) {
        return token != null && getAuthenticationTokenClass().isAssignableFrom(token.getClass());
    }
    
    public Class getAuthenticationTokenClass() {
        return authenticationTokenClass;
    }

    public void setAuthenticationTokenClass(Class<? extends AuthenticationToken> authenticationTokenClass) {
        this.authenticationTokenClass = authenticationTokenClass;
    }

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {		
        Set<String> permsSet = new HashSet<String>();
        permsSet.add("course:getdetail");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
	}


	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		Oauth2Token oauthToken = (Oauth2Token)token;
		String openid = oauthToken.getPrincipal();
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(openid, openid, getName());
        return info;
	}

}
