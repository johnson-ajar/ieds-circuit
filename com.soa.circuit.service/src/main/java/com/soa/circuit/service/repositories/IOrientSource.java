package com.soa.circuit.service.repositories;

public interface IOrientSource {
	 /**
     * Gets the source type.
     *
     * @return the source type
     */
    OrientSourceType getSourceType();
    
    /**
     * Gets the source name.
     *
     * @return the name
     */
    String getName();
}
