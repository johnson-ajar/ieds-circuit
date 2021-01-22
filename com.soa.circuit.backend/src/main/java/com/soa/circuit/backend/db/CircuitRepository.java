package com.soa.circuit.backend.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.orient.commons.repository.OrientSource;
import org.springframework.stereotype.Repository;

import com.soa.circuit.backend.rs.OrientGraphDBFactory;
import com.soa.circuit.common.ElementProperties;
import com.soa.circuit.persist.CircuitElementVertex;
import com.syncleus.ferma.AbstractVertexFrame;

@Repository
public class CircuitRepository implements ICircuitRepository{
	
	@Autowired
	private OrientGraphDBFactory ghFactory;

	@Override
	public <S extends CircuitElementVertex> S save(S entity, String cluster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count(String cluster) {
		Iterator<? extends AbstractVertexFrame> vertices= ghFactory.getFramedGraph().getFramedVertices(ElementProperties.CLASS.property(),
				CircuitElementVertex.class.getName(),CircuitElementVertex.class);
		int count = 0;
		while(vertices.hasNext()){
			CircuitElementVertex v = (CircuitElementVertex)vertices.next();
			if(v.getType().toString().equals(cluster)){
				count++;
			}
		}
		return count; 
	}

	@Override
	public long count(Class<? extends CircuitElementVertex> domainClass) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long count(OrientSource source) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Class<CircuitElementVertex> getDomainClass() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Optional<CircuitElementVertex> findById(String id) {
		return null;
	}
	
	@Override
	public List<CircuitElementVertex> findAll() {
		Iterator<? extends AbstractVertexFrame> vertices= ghFactory.getFramedGraph().getFramedVertices(ElementProperties.CLASS.property(),
				CircuitElementVertex.class.getName(),CircuitElementVertex.class);
		List<CircuitElementVertex> results = new ArrayList<CircuitElementVertex>();
		while(vertices.hasNext()){
			CircuitElementVertex v = (CircuitElementVertex)vertices.next();
			results.add(v);
		}
		System.out.println("The number of vertices "+results.size());
		return results;
	}

	//@Override
	public List<CircuitElementVertex> findAll(String cluster) {
		Iterator<? extends AbstractVertexFrame> vertices= ghFactory.getFramedGraph().getFramedVertices(ElementProperties.CLASS.property(),
				CircuitElementVertex.class.getName(),CircuitElementVertex.class);
		List<CircuitElementVertex> results = new ArrayList<CircuitElementVertex>();
		while(vertices.hasNext()){
			CircuitElementVertex v = (CircuitElementVertex)vertices.next();
			if(v.getType().toString().equals(cluster)){
				results.add(v);
			}
		}
		return results;
	}
	
	public List<CircuitElementVertex> findAllById(Iterable<String> ids) {
		return null;
	}
	
	@Override
	public List<CircuitElementVertex> findAll(OrientSource source) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends CircuitElementVertex> List<S> findAll(Class<S> domainClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CircuitElementVertex> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CircuitElementVertex> findAll(Iterable<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAll(String cluster) {
		Iterator<? extends AbstractVertexFrame> vertices= ghFactory.getFramedGraph().getFramedVertices(ElementProperties.CLASS.property(),
				CircuitElementVertex.class.getName(),CircuitElementVertex.class);
		while(vertices.hasNext()){
			CircuitElementVertex v = (CircuitElementVertex)vertices.next();
			if(v.getType().toString().equals(cluster)){
				v.remove();
			}
		}
	}

	@Override
	public void deleteAll(Class<? extends CircuitElementVertex> domainClass) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Page<CircuitElementVertex> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends CircuitElementVertex> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	public <S extends CircuitElementVertex> Iterable<S> save(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public <S extends CircuitElementVertex> Iterable<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public CircuitElementVertex findOne(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean existsById(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(CircuitElementVertex entity) {
		// TODO Auto-generated method stub
		
	}

	public void delete(Iterable<? extends CircuitElementVertex> entities) {
		// TODO Auto-generated method stub
		
	}

	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}
	
	public void deleteAll(Iterable<? extends CircuitElementVertex> entities) {
		// TODO Auto-generated method stub
		
	}
	
	public void deleteById(String id) {
		
	}

	public boolean exists(String id) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
