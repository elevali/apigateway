package io.eleva.apigateway.api.dto;

import io.eleva.apigateway.api.Response;

public class GenericResponse<T> implements Response{
	private Integer result;
	private String msg;
	
	public GenericResponse(){
		result = SUCCESS;
		msg = "success";
	}
    
    private T data;

	
	
	public Integer getResult() {
		return result;
	}



	public void setResult(Integer result) {
		this.result = result;
	}



	public String getMsg() {
		return msg;
	}



	public void setMsg(String msg) {
		this.msg = msg;
	}



	public T getData() {
		return data;
	}



	public void setData(T data) {
		this.data = data;
	}



	public static GenericResponse error(Integer result,String msg) {
		GenericResponse r = new GenericResponse();
		r.setResult(result);
		r.setMsg(msg);
		return r;
	}
	
	public static GenericResponse error(String msg) {
		GenericResponse r = new GenericResponse();
		r.setResult(FAILURE);
		r.setMsg(msg);
		return r;
	}
	
	public static GenericResponse success() {
		GenericResponse r = new GenericResponse();
		return r;
	}
	
	public GenericResponse<T> success(T data) {
		this.data = data;
		return this;
	}
}
