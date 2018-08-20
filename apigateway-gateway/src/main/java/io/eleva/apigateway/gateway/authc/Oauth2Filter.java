package io.eleva.apigateway.gateway.authc;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.eleva.apigateway.api.dto.Oauth2Response;
import io.eleva.apigateway.gateway.utils.HttpContextUtils;

public class Oauth2Filter extends AuthenticationFilter{

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());

        String json = new ObjectMapper().writeValueAsString(Oauth2Response.error(HttpStatus.UNAUTHORIZED.value(), "unauthorized request"));

        httpResponse.getWriter().print(json);

        return false;
	}	
	

}
