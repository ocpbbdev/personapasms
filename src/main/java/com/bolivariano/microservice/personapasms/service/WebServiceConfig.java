package com.bolivariano.microservice.personapasms.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.soap.Node;
import javax.xml.ws.BindingProvider;

import org.apache.camel.Exchange;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.headers.Header;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.wss4j.common.crypto.Merlin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tempuri.IServiceConsultaRCG;

import com.bolivariano.cashmanagement.ws.clientecash.Cliente;
import com.bolivariano.cashmanagement.ws.clientecash.ClienteCashBindingSOAP11QSService;
import com.bolivariano.microservice.personapasms.consumeWS.ServiceBL;
import com.bolivariano.ws.registrocivil.RegistroCivil;
import com.bolivariano.ws.registrocivil.RegistroCivilSOAPQSService;
import com.oracle.xmlns.sca_bloqueologinusuario.frameworkseguridad.frameworkmediator.FrameworkMediatorEp;
import com.oracle.xmlns.sca_bloqueologinusuario.frameworkseguridad.frameworkmediator.FrameworkPtt;


@Service
public class WebServiceConfig {

	Logger logger = LoggerFactory.getLogger(WebServiceConfig.class);

	@Value("${client.ws.address.registroCivil.url}")
	private String registroCivilOsbMensajeURL;
	@Value("${client.ws.address.registroCivil.conntimeout}")
	private int registroCivilOsbConntimeout;
	@Value("${client.ws.address.registroCivil.readtimeout}")
	private int registroCivilOsbReadtimeout;
	
	@Value("${client.ws.address.listaNegra.url}")
	private String listaNegraOsbMensajeURL;
	@Value("${client.ws.address.listaNegra.conntimeout}")
	private int listaNegraOsbConntimeout;
	@Value("${client.ws.address.listaNegra.readtimeout}")
	private int listaNegraOsbReadtimeout;	
	
	
	@Value("${client.ws.address.avisos24.url}")
	private String avisos24URL;
	@Value("${client.ws.address.avisos24.conntimeout}")
	private int avisos24Conntimeout;
	@Value("${client.ws.address.avisos24.readtimeout}")
	private int avisos24Readtimeout;

	
	
	
	@Value("${client.service.oracle.username}")
	private String busUsername;
	@Value("${client.service.oracle.password}")
	private String busPassword;
	@Value("${client.service.oracle.encrypUsername}")
	private String encrypUsername;
	


	@Value("${client.ssl.trustStore}")
	private String truestore;
	@Value("${client.ssl.trustStorePassword}")
	private String trustStorePassword;
	@Value("${client.ssl.type}")
	private String trustStoreType;	
	
	
	
	@Value("${client.ws.address.registrocivilM.url}")
	private String registrocivilURL;
	@Value("${client.ws.address.registrocivilM.conntimeout}")
	private int registrocivilConnTimeout;
	@Value("${client.ws.address.registrocivilM.readtimeout}")
	private int registrocivilReadTimeout;
	
	
	@Value("${client.ws.address.framework.url}")
	private String frameworkURL;
	@Value("${client.ws.address.framework.conntimeout}")
	private int frameworkConnTimeout;
	@Value("${client.ws.address.framework.readtimeout}")
	private int frameworkReadTimeout;
	
	
	
	@PostConstruct
	private void setSSL() {
		System.setProperty("javax.net.ssl.trustStore", truestore);
		System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
		try {			
			//clienteCash();
			//clienteHomologarMensaje();
			
			clienteRegistroCivilOsb();
			clienteListaNegraOsb();
			
		} catch (Exception e) {
			logger.error("error cargando los clientes webservice", e);
		}
	}

	private void setBusSecurity(BindingProvider bindingProvider) throws Exception{
		Map<String, Object> reqContext = bindingProvider.getRequestContext();
		reqContext.put("security.encryption.username", encrypUsername);
		reqContext.put("ws-security.username", busUsername);
		reqContext.put("ws-security.password", busPassword);
	}
	
	
	//private Cliente clienteClienteCash = null;
	//private HomologarMensaje clienteHomologarMensaje = null;
	private RegistroCivil clienteRegistroCivil = null;
	private ServiceBL clienteListaNegra = null;

	private void setBindingProviderSecurity(BindingProvider bindingProvider, String address) throws Exception {

		try {
			Map<String, Object> reqContext = bindingProvider.getRequestContext();
			reqContext.put("security.encryption.username", encrypUsername);
			reqContext.put("ws-security.username", busUsername);
			reqContext.put("ws-security.password", busPassword);
			reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
			reqContext.put("ws-security.is-bsp-compliant", "false");
			Merlin issuerCrypto = new Merlin();
			KeyStore keyStore = KeyStore.getInstance("JKS");
			File keyStoreFile = new File(truestore);
			InputStream input = new FileInputStream(keyStoreFile);
			keyStore.load(input, trustStorePassword.toCharArray());
			issuerCrypto.setKeyStore(keyStore);
			reqContext.put("security.signature.crypto", issuerCrypto);
			reqContext.put("security.encryption.crypto", issuerCrypto);

		} catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
			logger.error("error cargando el certificado de encriptacion del servicio", e);
			throw e;
		}
	}

	/*public Cliente clienteCash() throws Exception {
		if (clienteClienteCash == null) {
			ClienteCashBindingSOAP11QSService clienteCuentasService = new ClienteCashBindingSOAP11QSService(new URL(clienteCash));
			clienteClienteCash = clienteCuentasService.getClienteCashBindingSOAP11QSPort();
			setBindingProviderSecurity((BindingProvider) clienteClienteCash, clienteCash);
			setTimeouts((BindingProvider) clienteClienteCash, clienteCashConnTimeout, clienteCashReadTimeout);
		}
		return clienteClienteCash;
	}*/

	/*public HomologarMensaje clienteHomologarMensaje() throws Exception {
		if (clienteHomologarMensaje == null) {
			HomologarMensaje_Service homologarMensajeService = new HomologarMensaje_Service(
					new URL(homologarMensajeURL));
			clienteHomologarMensaje = homologarMensajeService.getHomologarMensajePt();
			setTimeouts((BindingProvider) clienteHomologarMensaje, homologarMensajeConntimeout,
					homologarMensajeReadtimeout);
		}
		return clienteHomologarMensaje;
	}*/
	
	public RegistroCivil clienteRegistroCivilOsb() throws Exception {
		if (clienteRegistroCivil == null) {
			RegistroCivilSOAPQSService registroCivilService = new RegistroCivilSOAPQSService(
					new URL(registroCivilOsbMensajeURL));	
			clienteRegistroCivil = registroCivilService.getRegistroCivilSOAPQSPort();
			setBusSecurity((BindingProvider) clienteRegistroCivil);
		    setBindingProviderSecurity((BindingProvider) clienteRegistroCivil, registroCivilOsbMensajeURL);
			setTimeouts((BindingProvider) clienteRegistroCivil, registroCivilOsbConntimeout,
					registroCivilOsbReadtimeout);
		}
		return clienteRegistroCivil;
	}
	
	public RegistroCivil clienteListaNegraOsb() throws Exception {
		if (clienteRegistroCivil == null) {
			RegistroCivilSOAPQSService registroCivilService = new RegistroCivilSOAPQSService(
					new URL(registroCivilOsbMensajeURL));
			clienteRegistroCivil = registroCivilService.getRegistroCivilSOAPQSPort();
			setTimeouts((BindingProvider) clienteRegistroCivil, registroCivilOsbConntimeout,
					registroCivilOsbReadtimeout);
		}
		return clienteRegistroCivil;
	}
	
	public ServiceBL clienteListaNegra() throws Exception {
		if (clienteListaNegra == null) {
			ServiceBL clienteCuentasService = new ServiceBL(new URL(listaNegraOsbMensajeURL));
			clienteListaNegra = (ServiceBL) clienteCuentasService.getPorts();
			setBindingProviderSecurity((BindingProvider) clienteListaNegra, listaNegraOsbMensajeURL);
			setTimeouts((BindingProvider) clienteListaNegra, listaNegraOsbConntimeout, listaNegraOsbReadtimeout);
		}
		return clienteListaNegra;
	}
	
	 org.tempuri.IServiceConsultaRCG registrocivil= null;
	    
	   
	    public IServiceConsultaRCG registrocivil() throws Exception{
	    	if(registrocivil == null) {
	    		org.tempuri.ServiceConsultasRCG registrocivilService =new org.tempuri.ServiceConsultasRCG();   
	   		 	registrocivil = registrocivilService.getBasicHttpBindingIServiceConsultaRCG();
	   		 	((BindingProvider) registrocivil).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, registrocivilURL);
	   		    setBusSecurity((BindingProvider) registrocivil);
	   		    setBindingProviderSecurity((BindingProvider) registrocivil, registrocivilURL);
		 	    setTimeouts((BindingProvider) registrocivil, registrocivilConnTimeout, registrocivilConnTimeout);    
	    	}
			return registrocivil;
	    }
	    
	    private FrameworkPtt frameworkPtt = null;
	    public FrameworkPtt clienteFramework() throws Exception{
	    	if(frameworkPtt == null) {
	    		FrameworkMediatorEp cuentapasService = new FrameworkMediatorEp(new URL(frameworkURL));
	    		frameworkPtt = cuentapasService.getFrameworkPt();
	    		setTimeouts((BindingProvider) frameworkPtt, frameworkConnTimeout, frameworkReadTimeout);
	    	}
			return frameworkPtt;
	    }
	    

		private Cliente clienteAvisos24 = null;
	    
	    public Cliente clienteAvisos24() throws Exception {
			if (clienteAvisos24 == null) {
				System.out.println("Antes de new BPMSolicitud");

				ClienteCashBindingSOAP11QSService clienteAvisosService = new ClienteCashBindingSOAP11QSService(
						new URL(avisos24URL));
				clienteAvisos24 = clienteAvisosService.getClienteCashBindingSOAP11QSPort();
				setBusSecurity((BindingProvider) clienteAvisos24);
				setBindingProviderSecurity((BindingProvider) clienteAvisos24, avisos24URL);
				setTimeouts((BindingProvider) clienteAvisos24, avisos24Conntimeout, avisos24Readtimeout);
			}
			return clienteAvisos24;
		}
	
	private void setTimeouts(BindingProvider serviceClass, int connTimeout, int readTimeout) {
		Client cxfClient = ClientProxy.getClient(serviceClass);
		HTTPConduit httpConduit = (HTTPConduit) cxfClient.getConduit();
		HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
		httpClientPolicy.setConnectionRequestTimeout(connTimeout);
		httpClientPolicy.setConnectionTimeout(connTimeout);
		httpClientPolicy.setReceiveTimeout(readTimeout);
		httpClientPolicy.setContentType("text/xml;charset=UTF-8");
		httpConduit.setClient(httpClientPolicy);
	}

	public void resetClient(Object client) {
		if (client instanceof ServiceBL) {
			clienteListaNegra = null;
		} /*else if (client instanceof HomologarMensaje) {
			clienteHomologarMensaje = null;
		}*/

	}
	
	public Map<String, String> getCustomHeaders(Exchange exchange){
		 Map<String, String> headers = null;
		 @SuppressWarnings("unchecked")
		 List<Header> customHeaders = (List<Header>) exchange.getMessage().getHeaders().get(Header.HEADER_LIST);
		 if(customHeaders!= null) {
			 headers = new HashMap<>();
			 for(Header header : customHeaders) {
				 Node element = (Node) header.getObject();
				 headers.put(header.getName().getLocalPart(), element.getFirstChild()!=null?element.getFirstChild().getNodeValue():"");
			 }
		 }
		 return headers; 
	}

}
