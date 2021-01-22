package com.soa.circuit.backend.rs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.orient.commons.core.OrientTransactionManager;
import org.springframework.data.orient.commons.repository.config.EnableOrientRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

@Configuration
@EnableTransactionManagement
@EnableOrientRepositories(basePackages="com.soa.circuit.backend.sample")
public class OrientGraphDBConfiguration {
	
	
	@Bean
	public OrientGraphDBFactory ghFactory(){
		String url = "remote:127.0.0.1:/resistor5";
		String userName = "admin";
		String password = "admin";
		
		OrientGraphDBFactory factory = new OrientGraphDBFactory(url, userName, password);
		factory.setUrl(url);
		factory.setUsername(userName);
		factory.setPassword(password);
		
		return factory;
	}
	
	
	@Bean
	public OrientTransactionManager transactionManager(){
		return new OrientTransactionManager(ghFactory());
	}
	
	@Bean
	public OrientGraphTemplate objectTemplate(){
		return new OrientGraphTemplate(ghFactory());
	}
	
	@Bean
	public int readVersion(){
		return 1;
	}
	
	
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/circuit/elements")
                		.allowedOrigins("http://localhost:4200");
            }
        };
    }
	
}
