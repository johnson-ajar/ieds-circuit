package com.soa.circuit.backend.sample;

import javax.persistence.Id;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(value = {"handler"})
public class Person {
	
	@Id
	private String id;
	
	@Version
	@JsonIgnore
	private Long version;
	
	private String detachAll;
	
	private String firstName;
	
	private String lastName;
	
	
	private Integer age;
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id  = id;
	}
	
	public String getFirstName(){
		return firstName;
	}
	
	public String getLastName(){
		return lastName;
	}
	
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	
	public Integer getAge(){
		return age;
	}
	
	public void setAge(Integer age){
		this.age = age;
	}
	
	public String toString(){
		String r = "";
		r="firstName : "+firstName;
		r+="lastName : "+lastName;
		r+="age : "+age;
		return r;
	}
}
