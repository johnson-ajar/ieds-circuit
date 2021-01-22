package com.soa.circuit.backend.sample;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/persons")
public class PersonEndPoint {
	
	@Autowired
	private PersonRepository repository;
	
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Person> findAllPersons(){
		return repository.findAll();
	}
	
	@GET
	@Path("/findByFirstName")
	@Produces("application/json")
	public List<Person> findByFirstName(@QueryParam("firstName") @NotNull String firstName){
		return repository.findByFirstName(firstName);
	}
	
	@GET
	@Path("/findByLastName")
	@Produces("application/json")
	public List<Person> findByLastName(@QueryParam("lastName") @NotNull String lastName){
		return repository.findByLastName(lastName);
	}
	
	@GET
	@Path("/findByAge")
	@Produces("application/json")
	public List<Person> findByAge(@QueryParam("age") @NotNull Integer age){
		return repository.findByAge(age);
	}
}
