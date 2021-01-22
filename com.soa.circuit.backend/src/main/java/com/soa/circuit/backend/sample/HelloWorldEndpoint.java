package com.soa.circuit.backend.sample;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soa.circuit.backend.rs.Service;

@Component
@Path("/hello")
public class HelloWorldEndpoint {
	private final Service service;
	private final ObjectMapper objectMapper;
	public HelloWorldEndpoint(Service service, ObjectMapper objectMapper){
		this.service = service;
		this.objectMapper = objectMapper;
	}
	@GET
	public String message(){
		return "Hello "+this.service.message();
	}
	
	
	private class SimpleClass {
		@SuppressWarnings("unused")
		public String foo="bar";
		@SuppressWarnings("unused")
		public String foo2 = "ba2r";
	}
	
	@GET
	@Path("/world")
	@Produces(MediaType.APPLICATION_JSON)
	public SimpleClass messageJson(){
		SimpleClass simpleClass = new SimpleClass();
		simpleClass.foo2 = objectMapper.toString();
		return simpleClass;
	}
	
	
	@GET()
	@Path("/worlds")
	@Produces(MediaType.APPLICATION_JSON)
	public List<SimpleClass> getWorlds(){
		List<SimpleClass> r = new ArrayList<SimpleClass>();
		SimpleClass c1 = new SimpleClass();
		c1.foo=objectMapper.toString();
		SimpleClass c2 = new SimpleClass();
		c2.foo2 = objectMapper.toString();
		r.add(c1);
		r.add(c2);
		return r;
	}
}
