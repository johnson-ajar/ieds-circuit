package com.soa.circuit.backend;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.orient.object.OrientObjectDatabaseFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.soa.circuit.backend.rs.OrientGraphDBFactory;
import com.soa.circuit.backend.sample.Person;
import com.soa.circuit.backend.sample.PersonRepository;


@EnableAutoConfiguration
@EnableTransactionManagement
@SpringBootApplication(
		scanBasePackages={"com.soa.circuit.backend.rs",
						  "com.soa.circuit.backend.db",
						  "com.soa.circuit.backend.rs.api",
						  "com.soa.circuit.backend.sample",
						  "com.soa.circuit.persist"
})
public class Application implements CommandLineRunner{
	
	@Autowired
	private PersonRepository repository;
	
	@Autowired
	private OrientObjectDatabaseFactory objFactory;
	
	@Autowired
	private OrientGraphDBFactory ghFactory;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Override
	public void run(String... args){
		OObjectDatabaseTx db = null;
		
		try{
			db = objFactory.openDatabase();
			ghFactory.openDatabase();
			db.getEntityManager().registerEntityClass(Person.class);
		}finally{
			if(db != null){
				db.close();
			}
		}
		
		
		//Create Persons if required
		if(repository.count() < 1){
			List<Person> persons = new ArrayList<Person>();
			
			 Person graham = new Person();
	            graham.setFirstName("Graham");
	            graham.setLastName("Jacobson");
	            graham.setAge(25);
	            repository.save(graham);
	            
	            persons.add(graham);
	            
	            Person ebony = new Person();
	            ebony.setFirstName("Ebony");
	            ebony.setLastName("Irwin");
	            ebony.setAge(21);
	            repository.save(ebony);
	            
	            persons.add(ebony);
	            
	            Person benedict = new Person();
	            benedict.setFirstName("Benedict");
	            benedict.setLastName("Preston");
	            benedict.setAge(25);
	            repository.save(benedict);
	            
	            persons.add(benedict);
	            
	            Person zorita = new Person();
	            zorita.setFirstName("Zorita");
	            zorita.setLastName("Clements");
	            zorita.setAge(23);
	            repository.save(zorita);
	            persons.add(zorita);
	            
	            Person kaitlin = new Person();
	            kaitlin.setFirstName("Kaitlin");
	            kaitlin.setLastName("Walter");
	            kaitlin.setAge(22);
	            repository.save(kaitlin);
	            persons.add(kaitlin);
	            
	            //repository.save(persons);
		}
		
	}
	
}
