package io.eleva.apigateway.gateway.authc;

import org.apache.shiro.authc.AuthenticationToken;

public class Oauth2Token implements AuthenticationToken{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4010258374381123956L;
	private String cookieid;
	
	

	public Oauth2Token(String cookieid) {
		super();
		this.cookieid = cookieid;
	}

	@Override
	public String getPrincipal() {
		// TODO Auto-generated method stub
		return cookieid;
	}

	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return cookieid;
	}

}
