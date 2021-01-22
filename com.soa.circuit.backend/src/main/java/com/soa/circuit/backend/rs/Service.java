package com.soa.circuit.backend.rs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Service {
	//default value
	@Value("${message:World}")
	private String msg;
	
	public String message(){
		return this.msg;
	}
}
