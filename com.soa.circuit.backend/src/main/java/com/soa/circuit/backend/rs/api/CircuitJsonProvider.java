package com.soa.circuit.backend.rs.api;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.soa.circuit.persist.CircuitElementVertex;


@Provider
@Produces(MediaType.APPLICATION_JSON)
public class CircuitJsonProvider implements MessageBodyWriter<CircuitElementVertex>{
	private static final String CHARSET = "charset";
	@Context
	protected Providers providers;
	
	private Class<?> getDomainClass(Type genericType) {
        if(genericType instanceof Class) {
            return (Class<?>) genericType;
        } else if(genericType instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
        } else {
            return null;
        }
    }

	private JAXBContext getJAXBContext(Class<?> type, MediaType mediaType)
	        throws JAXBException {
	        ContextResolver<JAXBContext> resolver
	            = providers.getContextResolver(JAXBContext.class, mediaType);
	        JAXBContext jaxbContext;
	        if(null == resolver || null == (jaxbContext = resolver.getContext(type))) {
	            return JAXBContext.newInstance(type);
	        } else {
	            return jaxbContext;
	        }
	}
	
	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return CircuitElementVertex.class == getDomainClass(genericType);
	}

	@Override
	public long getSize(CircuitElementVertex t, Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(CircuitElementVertex t, Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {
		Class<?> domainClass = getDomainClass(genericType);
		try{
			Marshaller m = getJAXBContext(domainClass, mediaType).createMarshaller();
			m.setProperty("eclipselink.media-type", MediaType.APPLICATION_JSON);
			m.setProperty("eclipselink.json.include-root", false);
			
			Map<String, String> mediaTypeParameters = mediaType.getParameters();
			if(mediaTypeParameters.containsKey(CHARSET)){
				String charSet = mediaTypeParameters.get(CHARSET);
				m.setProperty(Marshaller.JAXB_ENCODING, charSet);
			}
			m.marshal(t, entityStream);
		}catch(JAXBException jaxbException){
			throw new WebApplicationException("jaxbException");
		}
	}
		
}
