package com.soa.circuit.backend.rs;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.tinkerpop.gremlin.orientdb.OPartitionedReCreatableDatabasePool;
import org.apache.tinkerpop.gremlin.orientdb.OrientGraph;
import org.apache.tinkerpop.gremlin.orientdb.OrientGraphFactory;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.springframework.data.orient.commons.core.AbstractOrientDatabaseFactory;

import com.orientechnologies.orient.core.db.ODatabaseInternal;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.record.ORecord;
import com.syncleus.ferma.DelegatingFramedGraph;
import com.syncleus.ferma.FramedGraph;

public class OrientGraphDBFactory extends AbstractOrientDatabaseFactory<ORecord>{
	
	private OrientGraphFactory graphFactory; 
	
	private volatile OPartitionedReCreatableDatabasePool pool;
	
	private ODatabaseDocument db;
	
	private boolean labelAsClassName;
	
	private Configuration configuration;
	
	private FramedGraph fgraph;
	
	public OrientGraphDBFactory(String url, String username, String password){
			graphFactory = new OrientGraphFactory(url, username, password);
	}
	
	@Override
	protected void createPool() {
		 //since max pool size was set, use it to create the partitioned pool
        int maxPartitionSize = Runtime.getRuntime().availableProcessors();
        graphFactory.setupPool(maxPartitionSize,10);
        pool = graphFactory.pool();
	}

	@Override
	public ODatabaseDocument openDatabase() {
		 db = pool.acquire();
	     return db;
	}
	
	@Override
	public ODatabaseDocument db(){
		return (ODatabaseDocument)super.db();
	}

	@Override
	protected ODatabaseInternal<ORecord> newDatabase() {
		db = openDatabase();
        if (db == null || (!db.getURL().startsWith("remote:") && !db.exists())) {
        	return new ODatabaseDocumentTx(url);
        } 
        
        return null;
	}

	/**
     * Enable or disable the prefixing of class names with V_&lt;label&gt; for
     * vertices or E_&lt;label&gt; for edges.
     * 
     * @param is
     *            if true classname equals label, if false classname is prefixed
     *            with V_ or E_ (default)
     */
    public void setLabelAsClassName(boolean is) {
        this.labelAsClassName = is;
    }
    
    /**
     * @param create
     *            if true automatically creates database if database with given
     *            URL does not exist
     * @param open
     *            if true automatically opens the database
     */
    protected ODatabaseDocument getDatabase(boolean create, boolean open) {
         db = graphFactory.getDatabase(create, open);
        return db;
    }
    
    
    /**
     * Gets transactional graph with the database from pool if pool is
     * configured. Otherwise creates a graph with new db instance. The Graph
     * instance inherits the factory's configuration.
     *
     * @param create
     *            if true automatically creates database if database with given
     *            URL does not exist
     * @param open
     *            if true automatically opens the database
     */
    // TODO: allow to open with these properties
    public OrientGraph getNoTx(boolean create, boolean open) {
        return getGraph(create, open, false);
    }

    public OrientGraph getNoTx() {
        return getNoTx(true, true);
    }

    public OrientGraph getTx(boolean create, boolean open) {
        return getGraph(create, open, true);
    }

    public OrientGraph getTx() {
        return getTx(true, true);
    }

    protected OrientGraph getGraph(boolean create, boolean open, boolean transactional) {
        final OrientGraph g;
        final Configuration config = getConfiguration(create, open, transactional);
        if (pool != null) {
            g = transactional ? graphFactory.getTx() : graphFactory.getNoTx();
        } else {
            g = transactional ? graphFactory.getTx(create, open) : graphFactory.getNoTx(create, open);
        }
        initGraph(g);
        return g;
    }

    protected void initGraph(OrientGraph g) {
        final ODatabaseDocument db = g.getRawDatabase();
        boolean txActive = db.getTransaction().isActive();

        if (txActive)
            // COMMIT TX BEFORE ANY SCHEMA CHANGES
            db.commit();

        OSchema schema = db.getMetadata().getSchema();
        if (!schema.existsClass(OClass.VERTEX_CLASS_NAME))
            schema.createClass(OClass.VERTEX_CLASS_NAME).setOverSize(2);
        if (!schema.existsClass(OClass.EDGE_CLASS_NAME))
            schema.createClass(OClass.EDGE_CLASS_NAME);

        if (txActive) {
            // REOPEN IT AGAIN
            db.begin();
        }
    }

    protected Configuration getConfiguration(final boolean create,final boolean open,final boolean transactional) {
        if (configuration != null)
            return configuration;
        else
            return new BaseConfiguration() {
                {
                    setProperty(Graph.GRAPH, OrientGraph.class.getName());
                    setProperty(OrientGraph.CONFIG_URL, url);
                    setProperty(OrientGraph.CONFIG_USER, username);
                    setProperty(OrientGraph.CONFIG_PASS, password);
                    setProperty(OrientGraph.CONFIG_CREATE, create);
                    setProperty(OrientGraph.CONFIG_OPEN, open);
                    setProperty(OrientGraph.CONFIG_TRANSACTIONAL, transactional);
                    setProperty(OrientGraph.CONFIG_LABEL_AS_CLASSNAME, labelAsClassName);
                }
            };
    }
    
    public OPartitionedReCreatableDatabasePool pool() {
        return pool;
    }

    /**
     * Closes all pooled databases and clear the pool.
     */
    public void close() {
        if (pool != null)
            pool.close();

        pool = null;
    }
    
    public FramedGraph getFramedGraph(){
    	fgraph =  new DelegatingFramedGraph<OrientGraph>(this.getNoTx(), true, false);
    	return fgraph;
    }
}
