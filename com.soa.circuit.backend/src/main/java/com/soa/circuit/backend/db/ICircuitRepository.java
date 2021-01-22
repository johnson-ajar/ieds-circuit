package com.soa.circuit.backend.db;

import org.springframework.data.orient.graph.repository.OrientGraphRepository;

import com.soa.circuit.persist.CircuitElementVertex;

public interface ICircuitRepository extends OrientGraphRepository<CircuitElementVertex>{
	
}
