package io.eleva.apigateway.api;

public interface Response {
	
	public static final Integer SUCCESS = 1;
	public static final Integer FAILURE = -1;
	
	public Integer getResult();
	public void setResult(Integer result);
	public String getMsg();
	public void setMsg(String msg);

}
