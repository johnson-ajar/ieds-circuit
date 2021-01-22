package com.soa.circuit.service.repositories;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.soa.circuit.common.ElementProperties;
import com.soa.circuit.persist.CircuitElementVertex;
import com.soa.circuit.service.orientdb.OrientFramedGraphFactory;
import com.syncleus.ferma.AbstractVertexFrame;

@Repository
public class CircuitOrientDBRepository implements IOrientRepository<CircuitElementVertex>{
	
	@Autowired
	private OrientFramedGraphFactory ghFactory;
	
	@Override
	public <S extends CircuitElementVertex> S save(S entity, String cluster) {
		return null;
	}

	@Override
	public long count(String cluster) {
		System.out.println(cluster);
		Iterator<? extends AbstractVertexFrame> vertices= ghFactory.getFramedGraph().getFramedVertices(ElementProperties.CLASS.property(),
				CircuitElementVertex.class.getName(),CircuitElementVertex.class);
		int count = 0;
		while(vertices.hasNext()){
			CircuitElementVertex v = (CircuitElementVertex)vertices.next();
			System.out.println(v);
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
	public long count(IOrientSource source) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Class<CircuitElementVertex> getDomainClass() {
		// TODO Auto-generated method stub
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

	@Override
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

	@Override
	public List<CircuitElementVertex> findAll(IOrientSource source) {
		return null;
	}

	@Override
	public <S extends CircuitElementVertex> List<S> findAll(Class<S> domainClass) {
		return null;
	}

	@Override
	public List<CircuitElementVertex> findAll(Sort sort) {
		return null;
	}

	@Override
	public List<CircuitElementVertex> findAll(Iterable<String> ids) {
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
	}

}
