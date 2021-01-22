package com.soa.circuit.backend.sample;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.orient.commons.repository.OrientSource;
import org.springframework.data.orient.object.OrientObjectDatabaseFactory;
import org.springframework.stereotype.Repository;

import com.orientechnologies.orient.core.query.OQueryAbstract;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

@Repository
public class PersonRepository implements IPersonRepository {
	
	@Autowired
	private OrientObjectDatabaseFactory objFactory;
	
	@Override
	public <S extends Person> S save(S entity, String cluster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count(String cluster) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long count(Class<? extends Person> domainClass) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long count(OrientSource source) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Class<Person> getDomainClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Person> findAll() {
		System.out.println("Finding all the persons.");
		OQueryAbstract query = new OSQLSynchQuery<Person>("select * from person");
		List<Person> persons = objFactory.db().query(query);
		return persons;
	}

	@Override
	public List<Person> findAll(String cluster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Person> findAll(OrientSource source) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Person> List<S> findAll(Class<S> domainClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Person> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Person> findAll(Iterable<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAll(String cluster) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(Class<? extends Person> domainClass) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Page<Person> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Person> S save(S entity) {
		System.out.println("Saving the person "+entity.getFirstName());
		objFactory.db().begin();
		objFactory.db().save(entity);
		objFactory.db().commit();
		return null;
	}

	@Override
	public <S extends Person> Iterable<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Person> findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsById(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<Person> findAllById(Iterable<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteById(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Person entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(Iterable<? extends Person> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Person> findAllPersons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Person> findByFirstName(String firstName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Person> findByLastName(String lastName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Person> findByAge(Integer age) {
		// TODO Auto-generated method stub
		return null;
	}

}
