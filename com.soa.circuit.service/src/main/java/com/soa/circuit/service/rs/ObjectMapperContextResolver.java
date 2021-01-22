package com.soa.circuit.service.rs;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.soa.circuit.persist.CircuitElementVertex;

//CORS headers support
@Provider
@PreMatching
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper>, ContainerRequestFilter, ContainerResponseFilter {

	 private final ObjectMapper mapper;

	    @Autowired
	    public ObjectMapperContextResolver(ObjectMapper objectMapper) {
	        this.mapper = objectMapper;
	        SimpleModule module = new SimpleModule("circuitModule", new Version(1,0,0, null));
	        module.addSerializer(CircuitElementVertex.class, new CircuitElementVertexSerializer());
	        objectMapper.registerModule(module);
	    }

	    @Override
	    public ObjectMapper getContext(Class<?> type) {
	        return mapper;
	    }

		@Override
		public void filter(ContainerRequestContext requestContext) throws IOException {
			  System.out.println("Executing REST request filter"); 
			  requestContext.getHeaders().add( "Access-Control-Allow-Credentials", "true" );
	          requestContext.getHeaders().add( "Access-Control-Allow-Origin", "http://localhost:4200");
	          requestContext.getHeaders().add( "Access-Control-Allow-Methods", "OPTIONS, GET, POST, DELETE, PUT" );
	          requestContext.getHeaders().add( "Access-Control-Allow-Headers", "Content-Type" );
		}

		@Override
		public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
				throws IOException {
			System.out.println("Executing REST response filter");
			  responseContext.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:4200");
		      //responseContext.getHeaders().add("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, authorization");
		      responseContext.getHeaders().add("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Allow-Origin, Access-Control-Allow-Methods");
			  responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
		      responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
		      responseContext.getHeaders().add("Access-Control-Max-Age", "1209600");
		}

}
