package io.eleva.apigateway.api;

public interface Request {
	public String getUserid();
	public void setUserid(String userid);
	public String getReqaddr();
	public void setReqaddr(String reqaddr);
	public String getSequence();
	public void setSequence(String sequence);
	public String getToken();
	public void setToken(String token);	
	public String getVersion();
	public void setVersion(String version);

}
