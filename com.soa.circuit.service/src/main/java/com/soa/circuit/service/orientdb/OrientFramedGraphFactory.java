package com.soa.circuit.service.orientdb;

import javax.annotation.PostConstruct;

import org.apache.tinkerpop.gremlin.orientdb.OrientGraph;
import org.apache.tinkerpop.gremlin.orientdb.OrientGraphFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.syncleus.ferma.DelegatingFramedGraph;
import com.syncleus.ferma.FramedGraph;

@Component
public class OrientFramedGraphFactory {
	private OrientGraphFactory gFactory;
	private DelegatingFramedGraph framedGraph;
	
	@Autowired
	private OrientDBConnectionSettings settings;
	
	public OrientFramedGraphFactory() {	
	}
	
	@PostConstruct
	public void init() {
		gFactory = new OrientGraphFactory(settings.getUrl(), settings.getUsr(), settings.getPwd());
		OrientGraph g = gFactory.getNoTx();
		framedGraph = new DelegatingFramedGraph(g, true, false);
	}
	
	public FramedGraph getFramedGraph() {
		return framedGraph;
	}
}
