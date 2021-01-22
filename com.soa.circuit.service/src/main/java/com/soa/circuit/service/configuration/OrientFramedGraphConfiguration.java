package com.soa.circuit.service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.soa.circuit.service.orientdb.OrientFramedGraphFactory;
import com.soa.circuit.service.repositories.CircuitOrientDBRepository;

@Configuration
public class OrientFramedGraphConfiguration {
	
	@Bean
	public OrientFramedGraphFactory ghFactory() {
		return new OrientFramedGraphFactory();
	}
	
	@Bean
	public CircuitOrientDBRepository respository() {
		return new CircuitOrientDBRepository();
	}
}
