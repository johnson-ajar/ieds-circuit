package com.soa.circuit.backend.rs;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.orient.commons.core.OrientTransactionManager;
import org.springframework.data.orient.commons.repository.config.EnableOrientRepositories;
import org.springframework.data.orient.object.OrientObjectDatabaseFactory;
import org.springframework.data.orient.object.OrientObjectTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.soa.circuit.backend.sample.Person;

@Configuration
@EnableTransactionManagement
@EnableOrientRepositories(basePackages="com.soa.circuit.backend.sample")
public class OrientObjectDBConfiguration {
	
	@Bean
	public OrientObjectDBFactory objFactory(){
		OrientObjectDBFactory factory = new OrientObjectDBFactory();
		factory.setUrl("remote:127.0.0.1/Person");
		factory.setUsername("admin");
		factory.setPassword("admin");
		return factory;
	}
	
	@Bean
	public OrientTransactionManager transactionManager(){
		return new OrientTransactionManager(objFactory());
	}
	
	@Bean
	public OrientObjectTemplate objectTemplate(){
		return new OrientObjectTemplate(objFactory());
	}
	
	/*@Bean
	public int readVersion(){
		return 1;
	}*/
	
	@PostConstruct
	public void registerEntities(){
		objFactory().db().getEntityManager().registerEntityClass(Person.class);
	}
	
}
