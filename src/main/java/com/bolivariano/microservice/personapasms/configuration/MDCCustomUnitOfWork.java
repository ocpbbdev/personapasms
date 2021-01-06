package com.bolivariano.microservice.personapasms.configuration;

import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.camel.Exchange;
import org.apache.camel.impl.MDCUnitOfWork;
import org.apache.cxf.message.MessageContentsList;
import org.slf4j.MDC;

public class MDCCustomUnitOfWork extends MDCUnitOfWork {
    public MDCCustomUnitOfWork(Exchange exchange) {
        super(exchange);
        XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();
		if(exchange.getIn().getBody() instanceof MessageContentsList) {
			MessageContentsList list = (MessageContentsList) exchange.getIn().getBody();
			DOMSource source = (DOMSource) list.get(0);
			String secuencial = "";
			try {
				secuencial = (String)  xPath.evaluate("//secuencial", source.getNode(), XPathConstants.STRING);
			} catch (XPathExpressionException e) {
				secuencial = System.currentTimeMillis()+"";
			}
	        MDC.put("secuencial", secuencial);
	        exchange.setProperty("secuencial", secuencial);
		}else {
			if(exchange.getProperty("secuencial") != null) {
				String secuencial = (String) exchange.getProperty("secuencial");
				MDC.put("secuencial", secuencial);	
			}
		}
    }
}