package com.soa.circuit.backend.rs;

import org.springframework.data.orient.object.OrientObjectDatabaseFactory;

import com.orientechnologies.orient.object.db.OObjectDatabasePool;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

public class OrientObjectDBFactory extends OrientObjectDatabaseFactory {
	
	private OObjectDatabasePool pool;

    /** The database. */
    private OObjectDatabaseTx db;

    @Override
    protected void createPool() {
        pool = new OObjectDatabasePool(getUrl(), getUsername(), getPassword());
        
        pool.setup(10, maxPoolSize);
    }

    @Override
    public OObjectDatabaseTx openDatabase() {
        db = pool.acquire();
        return db;
    }

    @Override
    public OObjectDatabaseTx db() {
        return (OObjectDatabaseTx) super.db();
    }

    @Override
    protected OObjectDatabaseTx newDatabase() {
        return new OObjectDatabaseTx(getUrl());
    }
    
}
