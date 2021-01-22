package com.soa.circuit.service.rs;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.soa.circuit.persist.CircuitElementVertex;
import com.soa.circuit.service.repositories.CircuitOrientDBRepository;

@Component
@CrossOrigin(origins = "http://localhost:4200")
@Path("/circuit")
public class CircuitEndPoint {
	@Autowired
	CircuitOrientDBRepository repository;
	
	
	public CircuitEndPoint(){
		
	}
	
	//http://localhost:8082/api/circuit/count?elementType=RESISTOR
	@GET
	@CrossOrigin(origins = "http://localhost:4200")
	@Path("/count")
	public long count(@QueryParam("elementType") @NotNull String elementType){
		return repository.count(elementType);
	}
	
	@GET
	@CrossOrigin(origins = "http://localhost:4200")
	@Path("/elements")
	@Produces(MediaType.APPLICATION_JSON)
	public List<CircuitElementVertex> getElements(){
		return repository.findAll();
	}
}
