package io.eleva.apigateway.api.dto;

import io.eleva.apigateway.api.Request;

public class GenericRequest<T> implements Request{
	private String userid;
	private String reqaddr;
	private String sequence;
	private String token;	
	private String version;
	
	//private String sign;    
	private T data;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getReqaddr() {
		return reqaddr;
	}

	public void setReqaddr(String reqaddr) {
		this.reqaddr = reqaddr;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	

}
