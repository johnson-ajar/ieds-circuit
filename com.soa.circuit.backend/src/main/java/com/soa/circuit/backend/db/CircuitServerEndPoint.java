package com.soa.circuit.backend.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

@Component
@Path("/server")
public class CircuitServerEndPoint {
	private final HttpClient httpClient;
	private final String baseURL = "http://127.0.0.1:2480/";
	public CircuitServerEndPoint(){
		httpClient = HttpClientBuilder.create().build();
		
	}
	
	@GET
	@Path("/listDatabases")
	@Produces(MediaType.APPLICATION_JSON)
	public String listDatabases(){
		HttpGet request = new HttpGet(baseURL+"listDatabases");
		request.addHeader("accept", "application/json");
		String line = "";
		String output  = "";
		try{
			HttpResponse response = httpClient.execute(request);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
			
			while((line= br.readLine()) != null){
				output+=line;
			}
		}catch(ClientProtocolException cpe){
			cpe.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		System.out.println(output);
		return output;
	}
}
