package io.eleva.apigateway.api.dto;

import io.eleva.apigateway.api.Response;

public class Oauth2Response<T> implements Response{
	private Integer result;
	private String msg;
	private String userid;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}

	private T data;

	
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
	public static Oauth2Response error(Integer result,String msg) {
		Oauth2Response r = new Oauth2Response();
		r.setResult(result);
		r.setMsg(msg);
		return r;
	}
	
	public static Oauth2Response error(String msg) {
		Oauth2Response r = new Oauth2Response();
		r.setResult(FAILURE);
		r.setMsg(msg);
		return r;
	}
	
	public static Oauth2Response success() {
		Oauth2Response r = new Oauth2Response();
		return r;
	}
	
	public Oauth2Response<T> success(T data) {
		this.data = data;
		return this;
	}
	@Override
	public Integer getResult() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setResult(Integer result) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getMsg() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setMsg(String msg) {
		// TODO Auto-generated method stub
		
	}

}
