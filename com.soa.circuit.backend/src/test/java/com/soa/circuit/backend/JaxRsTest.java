package com.soa.circuit.backend;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.orient.commons.repository.config.EnableOrientRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.soa.circuit.backend.sample.IPersonRepository;

@Configuration
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT /*,
classes={PersonRepository.class}*/)
@EnableOrientRepositories(basePackages={"com.soa.circuit.backend.rs","com.soa.circuit.backend.sample"})
public class JaxRsTest {
	@LocalServerPort
	private int port;
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private IPersonRepository personRepository;
	
	private String getUrl(){
		return "http://localhost:"+port+"/api";
	}
	@Test
	public void contextLoads() {
		System.out.println("contextLoads");
		ResponseEntity<String> entity = this.restTemplate.getForEntity(getUrl()+"/hello", String.class);
		Assertions.assertThat(entity.getBody()).isEqualTo("Hello World");
	}
	
	@Test
	public void reverse(){
		System.out.println("reverse");
		ResponseEntity<String> entity = this.restTemplate.getForEntity(getUrl()+"/reverse?input=olleh", String.class);
		Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(entity.getBody()).isEqualTo("hello");
	}
	
	@Test
	public void validation(){
		ResponseEntity<String> entity = this.restTemplate.getForEntity(getUrl()+"api/reverse", String.class);
		Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

}
