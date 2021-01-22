package com.soa.circuit.backend.rs;

import org.springframework.data.orient.commons.core.AbstractOrientOperations;
import org.springframework.data.orient.document.OrientDocumentOperations;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.ORecord;

public class OrientGraphTemplate extends AbstractOrientOperations<ORecord> implements OrientDocumentOperations{

	protected OrientGraphTemplate(OrientGraphDBFactory dbf) {
		super(dbf);
	}

	@Override
	public String getRid(ORecord entity) {
		return entity.getRecord().getIdentity().toString();
	}

	@Override
	public <RET> RET detach(RET entity) {
		((ORecord)entity).detach();
		return entity;
	}

	@Override
	public <RET> RET detachAll(RET entity) {
		return detach(entity);
	}

	@Override
	public ODatabaseDocumentTx getDocumentDatabase() {
		return (ODatabaseDocumentTx)dbf.db();
	}

}
