package com.soa.circuit.backend.sample;

import java.util.List;

import org.springframework.data.orient.commons.repository.annotation.Query;
import org.springframework.data.orient.object.repository.OrientObjectRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IPersonRepository extends OrientObjectRepository<Person>{
	
	@Query("select * from person")
	List<Person> findAllPersons();
	
	//@Query("select from person where firstName = ?")
	List<Person> findByFirstName(String firstName);
	
	//@Query("select from person where lastName = ?")
	List<Person> findByLastName(String lastName);
	
	//@Query("select from person where age = ?")
	List<Person> findByAge(Integer age);
}
