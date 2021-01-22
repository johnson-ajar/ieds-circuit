package com.soa.circuit.backend.rs.api;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.soa.circuit.persist.CircuitElementVertex;

public class CircuitElementVertexSerializer  extends JsonSerializer<CircuitElementVertex>{
	
	public CircuitElementVertexSerializer(){
		
	}
	
	@Override
	public void serialize(CircuitElementVertex value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		gen.writeString(value.toJson().toString());
	}

}
