package com.bolivariano.microservice.personapasms.cxf;

import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.Provider;
import javax.xml.ws.Service.Mode;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;

@WebServiceProvider
@ServiceMode(Mode.PAYLOAD)
public class SoapServiceProvider implements Provider<DOMSource>{

	@Override
	public DOMSource invoke(DOMSource request) {
		// TODO Auto-generated method stub
		return null;
	}

}
