package com.soa.circuit.service.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IOrientRepository<T> {
	/**
     * Saves a given entity to the given cluster. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity the entity
     * @param cluster the cluster name
     * @return the saved entity
     */
    <S extends T> S save(S entity, String cluster);
    
    /**
     * Returns the number of entities available with the given cluster.
     * 
     * @return the number of entities
     */
    long count(String cluster);

    /**
     * Returns the number of entities available with the given type.
     * 
     * @return the number of entities
     */
    long count(Class<? extends T> domainClass);
    
    /**
     * Returns the number of entities available with the given {@link OrientSource}.
     *
     * @param source the source
     * @return the long
     */
    long count(IOrientSource source);
    
    /**
     * Gets the domain class for repository.
     *
     * @return the domain class
     */
    Class<T> getDomainClass();
    
    /* (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#findAll()
     */
    List<T> findAll();
    
    /**
     * Returns all instances of the type with the given cluster.
     *
     * @param cluster the cluster name
     * @return the list
     */
    List<T> findAll(String cluster);
    
    /**
     * Returns all instances of the type with the given source.
     *
     * @param source the source
     * @return the list
     */
    List<T> findAll(IOrientSource source);
    
    /**
     * Returns all instances with the given type.
     *
     * @param domainClass the domain class
     * @return the list
     */
    <S extends T> List<S> findAll(Class<S> domainClass);
    
    /* (non-Javadoc)
     * @see org.springframework.data.repository.PagingAndSortingRepository#findAll(org.springframework.data.domain.Sort)
     */
    
    List<T> findAll(Sort sort);
    
    /* (non-Javadoc)
     * @see org.springframework.data.repository.CrudRepository#findAll(java.lang.Iterable)
     */
   
    List<T> findAll(Iterable<String> ids);
    
    /**
     * Deletes all entities managed by the repository for the given cluster.
     *
     * @param cluster the cluster name
     */
    void deleteAll(String cluster);
    
    /**
     * Deletes all entities with the given type managed by the repository.
     *
     * @param domainClass the domain class
     */
    void deleteAll(Class<? extends T> domainClass);
}
