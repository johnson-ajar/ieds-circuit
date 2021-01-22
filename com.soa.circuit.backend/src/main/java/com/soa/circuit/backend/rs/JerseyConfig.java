package com.soa.circuit.backend.rs;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.soa.circuit.backend.db.CircuitServerEndPoint;
import com.soa.circuit.backend.db.ObjectMapperContextResolver;
import com.soa.circuit.backend.rs.api.CircuitEndPoint;
import com.soa.circuit.backend.rs.api.CircuitJsonProvider;
import com.soa.circuit.backend.sample.HelloWorldEndpoint;
import com.soa.circuit.backend.sample.PersonEndPoint;
import com.soa.circuit.backend.sample.ReverseEndPoint;
import com.soa.circuit.persist.CircuitElementVertex;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;

@Component
public class JerseyConfig extends ResourceConfig{
	
	public JerseyConfig(){
		registerPackages();
		registerEndpoints();
		registerProviders();
		configureSwagger();
	}
	
	private void registerPackages() {
		register(CircuitElementVertex.class);
		packages("com.soa.circuit.persist");
	}
	private void registerProviders(){
		register(CircuitJsonProvider.class);
	}
	
	private void registerEndpoints(){
		register(HelloWorldEndpoint.class);
		register(ReverseEndPoint.class);
		register(PersonEndPoint.class);
		register(CircuitEndPoint.class);
		register(CircuitServerEndPoint.class);
		register(ObjectMapperContextResolver.class);
	}
	
    private void configureSwagger() {

        register(ApiListingResource.class);

        BeanConfig beanConfig = new BeanConfig();

        beanConfig.setVersion("1.0.2");

        beanConfig.setSchemes(new String[]{"http"});

        beanConfig.setHost("localhost:8082");

        beanConfig.setBasePath("/");

        beanConfig.setResourcePackage("com.soa.circuit.backend.rs.api");

        beanConfig.setPrettyPrint(true);

        beanConfig.setScan(true);

    }
}
