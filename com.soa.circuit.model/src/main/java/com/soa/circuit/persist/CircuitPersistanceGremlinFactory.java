package com.soa.circuit.persist;

import java.util.Iterator;

import org.apache.tinkerpop.gremlin.orientdb.OrientGraph;
import org.apache.tinkerpop.gremlin.orientdb.OrientGraphFactory;

import com.orientechnologies.orient.core.db.ODatabase;
import com.orientechnologies.orient.core.db.ODatabase.STATUS;
import com.orientechnologies.orient.core.exception.OConfigurationException;
import com.orientechnologies.orient.core.exception.ODatabaseException;
import com.soa.circuit.common.CircuitElement;
import com.soa.circuit.common.ElementProperties;
import com.soa.circuit.model.CircuitModel;
import com.syncleus.ferma.AbstractVertexFrame;
import com.syncleus.ferma.DelegatingFramedGraph;
import com.syncleus.ferma.FramedGraph;

public class CircuitPersistanceGremlinFactory {
	private static CircuitPersistanceGremlinFactory instance = null;
	private OrientGraph ograph;
	private FramedGraph fgraph;
	private boolean dbExists;
	
	private CircuitPersistanceGremlinFactory(){
		
	}
	
	public static CircuitPersistanceGremlinFactory getInstance(){
		if(instance == null){
			instance = new CircuitPersistanceGremlinFactory();
		}
		return instance;
	}
	
	public OrientGraphFactory connect(String url, String userName, String password){
		OrientGraphFactory remoteGraph = new OrientGraphFactory(url, userName,password).setupPool(1, 10);
		
		try{
			if(remoteGraph.isOpen()) {
				try{
					ograph = remoteGraph.getNoTx();
				}catch(ODatabaseException ex){
					ograph=	remoteGraph.getNoTx(true, true);
				}
			}
			ODatabase d = ograph.database().activateOnCurrentThread();
			if(d.isActiveOnCurrentThread()) {
				dbExists = true;
			}
			fgraph =  new DelegatingFramedGraph<OrientGraph>(ograph, true, false);
			System.out.println("Connect to the graph database : "+url);
		}catch(ODatabaseException ex){
			ex.printStackTrace();
		}
		return remoteGraph;
	}
	
	public boolean databaseExists(){
		return dbExists;
	}
	
	public boolean containClass(Class<?> clazz){
		return fgraph.getFramedVerticesExplicit(clazz).hasNext();
	}
	
	public FramedGraph getFramedGraph(){
		return fgraph;
	}
	
	public void dropDatabase(){
		if(dbExists){
			ograph.database().drop();
		}
	}
	

	public <T extends CircuitElement> CircuitElementVertex save(CircuitElementVertexInitializer<T> initializer){
		return fgraph.addFramedVertexExplicit(initializer);
	}
	
	public CircuitNodeVertex save(CircuitNodeVertexInitializer initializer){
		return fgraph.addFramedVertexExplicit(initializer);
	}
	

	public void load(CircuitModel model){
		if(ograph.isClosed()){
			return;
		}
		
		Iterator<? extends AbstractVertexFrame> elements = fgraph.getFramedVertices(ElementProperties.CLASS.property(),
															CircuitElementVertex.class.getName(),
															CircuitElementVertex.class);
		while(elements.hasNext()){
				CircuitElementVertex element = (CircuitElementVertex)elements.next();
				System.out.println("Element Name : "+element.getProperty(ElementProperties.NAME.property()));
				ElementFactory.getInstance().loadElement(model, element);		
		}
		
		Iterator<? extends AbstractVertexFrame> nodes = fgraph.getFramedVertices(
											ElementProperties.CLASS.property(),
											CircuitNodeVertex.class.getName(),
											CircuitNodeVertex.class);
		while(nodes.hasNext()){
			CircuitNodeVertex node = (CircuitNodeVertex)nodes.next();
			System.out.println("Node id :"+node.getNodeId());
			ElementFactory.getInstance().loadNode(model, node);
		}
		
		//connect elements to the node 
		for(CircuitElement element : model.getElements()){
			element.connectToNodes();
		}
	}
	
	public void connectToNodes(CircuitElement element){
		System.out.println(element.getName()+element.getType().inE());
		
        element.getVertex().traverse((v)->v.in(element.getName()+element.getType().inE())).toListExplicit(CircuitNodeVertex.class);
		CircuitNodeVertex inNode = element.getVertex().traverse(v->v.in(element.getName()+element.getType().inE())).next(CircuitNodeVertex.class);
		CircuitNodeVertex outNode = element.getVertex().traverse(v->v.out(element.getName()+element.getType().outE())).next(CircuitNodeVertex.class);
		
		element.addNodes(inNode.getNodeId(), outNode.getNodeId());
	}
}
