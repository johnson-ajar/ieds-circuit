package com.soa.circuit.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.soa.circuit.service.orientdb.OrientFramedGraphFactory;

@EnableAutoConfiguration
@EnableTransactionManagement
@SpringBootApplication(
		scanBasePackages={"com.soa.circuit.service.rs",
						  "com.soa.circuit.service.repositories",
						  "com.soa.circuit.service.orientdb"
})
public class CircuitApplication implements CommandLineRunner{
		
	@Autowired
	private OrientFramedGraphFactory ghFactory;
	
	public static void main(String[] args) {
		SpringApplication.run(CircuitApplication.class, args);
	}
	
	@Override
	public void run(String... args){
		
	}
	
}
