package io.eleva.apigateway.gateway.exception;

public class SpiKeyDuplicationException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3892234902114036663L;

	
	public SpiKeyDuplicationException() {
		
	}
	public SpiKeyDuplicationException(String clazz1,String clazz2) {
		super(clazz1+" with the same key as "+clazz2);
	}
}
