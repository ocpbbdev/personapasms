package com.bolivariano.microservice.personapasms.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class Util {
	private final static Logger logger = LoggerFactory.getLogger(Util.class);
	@Autowired	
	private ResourceLoader resourceLoader;
	   
	public InputStream getResourceAsInputStream(String resourceString) {
	    Resource resource = resourceLoader.getResource(resourceString);
        try {
			InputStream inputStream = resource.getInputStream();
			return inputStream;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
	}
	
	public String getResourceAsString(String resourceString) {
		InputStream inputStream = getResourceAsInputStream(resourceString);
		try {
			return convert(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public String convert(InputStream inputStream) throws IOException {
		   ByteArrayOutputStream result = new ByteArrayOutputStream();
		   byte[] buffer = new byte[1024];
		   int length;
		   while ((length = inputStream.read(buffer)) != -1) {
		       result.write(buffer, 0, length);
		   }
		   return result.toString("UTF-8");
	}
	

	public String convertirCadaPalabraEnMayuscula(String cadena) {
		char[] caracteres = cadena.toCharArray();
		caracteres[0] = Character.toUpperCase(caracteres[0]);
		 for (int i = 0; i < cadena.length()- 2; i++) 
		    // Es 'palabra'
		    if (caracteres[i] == ' ' || caracteres[i] == '.' || caracteres[i] == ','|| caracteres[i] == '(')
		      // Reemplazamos
		      caracteres[i + 1] = Character.toUpperCase(caracteres[i + 1]);
			  
		return new String(caracteres);
	}
	
	public String getStackTrace(Exception ex) {
		StringBuffer sb = new StringBuffer(500);
		StackTraceElement[] st = ex.getStackTrace();
		sb.append(ex.getClass().getName() + ": " + ex.getMessage() + "\n");
		for (int i = 0; i < st.length; i++) {
			sb.append("\t at " + st[i].toString() + "\n");
		}
		return sb.toString();
	}
}
