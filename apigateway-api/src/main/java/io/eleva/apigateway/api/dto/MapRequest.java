package io.eleva.apigateway.api.dto;

import java.util.HashMap;

import io.eleva.apigateway.api.Request;

public class MapRequest extends HashMap<String, Object> implements Request{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5821952878931910950L;

	@Override
	public String getUserid() {
		return get("userid").toString();
	}

	@Override
	public void setUserid(String userid) {
		put("userid",userid);
	}

	@Override
	public String getReqaddr() {
		return get("reqaddr").toString();
	}

	@Override
	public void setReqaddr(String reqaddr) {
		put("reqaddr",reqaddr);
	}

	@Override
	public String getSequence() {
		return get("sequence").toString();
	}

	@Override
	public void setSequence(String sequence) {
		put("sequence",sequence);
	}

	@Override
	public String getToken() {
		return get("token").toString();
	}

	@Override
	public void setToken(String token) {
		put("token",token);
	}

	@Override
	public String getVersion() {
		return get("version").toString();
	}

	@Override
	public void setVersion(String version) {
		put("version",version);
	}
	

}
