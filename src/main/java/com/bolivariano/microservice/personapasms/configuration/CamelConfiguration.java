package com.bolivariano.microservice.personapasms.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.CamelContext;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.model.RoutesDefinition;
import org.apache.camel.spring.CamelBeanPostProcessor;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.apache.cxf.ws.policy.PolicyBuilder;
import org.apache.cxf.ws.policy.PolicyConstants;
import org.apache.neethi.Policy;
import org.apache.wss4j.common.crypto.Merlin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xml.sax.SAXException;

import com.bolivariano.microservice.personapasms.cxf.ServerPasswordHandler;
import com.bolivariano.microservice.personapasms.cxf.SoapServiceProvider;

@Configuration
public class CamelConfiguration {
	Logger logger = LoggerFactory.getLogger(CamelConfiguration.class);
	
	@Autowired
    private ApplicationContext applicationContext;
	@Autowired
	ServerPasswordHandler serverPasswordHandler;

	@Value("${orchestrator.context-root}")
	private String contextRoot;
	@Value("${orchestrator.camel.tracing}")
	private boolean tracing;
	@Value("${orchestrator.camel.route}")
	private String route;
	@Value("${orchestrator.camel.policy}")
	private String policy;
	@Value("${orchestrator.endpoint.wsdl}")
	private String endpointWsdl;
	@Value("${orchestrator.endpoint.name}")
	private String endpointName;
	@Value("${orchestrator.endpoint.service}")
	private String endpointService;
	@Value("${orchestrator.endpoint.address}")
	private String endpointAddress;
	@Value("${orchestrator.endpoint.address-no-secure}")
	private String endpointAddressNoSecure;
	@Value("${server.security.encrypUsername}")
	private String encrypUsername;
	@Value("${client.ssl.trustStore}")
	private String truestore;
	@Value("${client.ssl.trustStorePassword}")
	private String trustStorePassword;
	
	@Bean(name="endpointSOAP")
    public CxfEndpoint endpointSOAP(){
		CxfEndpoint cxfEndpoint = createEndpoint(endpointAddressNoSecure);
        return cxfEndpoint;
    }
	
	@Bean(name="endpointSOAPSecure")
    public CxfEndpoint endpointSOAPSecured(){
		CxfEndpoint cxfEndpoint = createEndpoint(endpointAddress);
        try {
        	Map<String, Object> properties =  new HashMap<>();
            properties.put("security.callback-handler", serverPasswordHandler);
            properties.put("security.encryption.username", encrypUsername);
            properties.put("ws-security.is-bsp-compliant", "false");
            Merlin issuerCrypto = new Merlin();
            KeyStore keyStore = KeyStore.getInstance("JKS");
            InputStream input =Files.newInputStream(Paths.get(truestore));
	        keyStore.load(input, trustStorePassword.toCharArray());
	        issuerCrypto.setKeyStore(keyStore);
	        properties.put("security.signature.crypto", issuerCrypto);
	        properties.put("security.encryption.crypto", issuerCrypto);
	        
	        Bus bus = BusFactory.getDefaultBus();
			InputStream is = this.getClass().getResourceAsStream("/"+policy);
			PolicyBuilder builder = bus.getExtension(org.apache.cxf.ws.policy.PolicyBuilder.class);
			Policy wsSecuritypolicy = null;
			
	        wsSecuritypolicy = builder.getPolicy(is);
			properties.put(PolicyConstants.POLICY_OVERRIDE, wsSecuritypolicy);
			cxfEndpoint.setProperties(properties);
		} catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | ParserConfigurationException | SAXException e) {
			logger.error("error cargando el certificado de encriptacion o creando las politicas de seguridad del servicio", e);
			logger.error("servicio no se encuentra asegurado");
		}
        return cxfEndpoint;
    }
	
	private CxfEndpoint createEndpoint(String address) {
		CxfEndpoint cxfEndpoint = new CxfEndpoint();
        cxfEndpoint.setAddress(address);
        cxfEndpoint.setWsdlURL(endpointWsdl);
        cxfEndpoint.setServiceClass(SoapServiceProvider.class);
        cxfEndpoint.setEndpointNameString(endpointName);
        cxfEndpoint.setServiceNameString(endpointService);
        return cxfEndpoint;
	}
	
	@Bean(destroyMethod = "stop")
    public CamelContext camelContext() throws Exception {		
		SpringCamelContext camelContext = new SpringCamelContext(applicationContext);
		InputStream is = this.getClass().getResourceAsStream(route.replace("classpath:", "/"));
        RoutesDefinition routes = camelContext.loadRoutesDefinition(is);
        camelContext.addRouteDefinitions(routes.getRoutes());
        camelContext.setUseMDCLogging(true);
        camelContext.setUnitOfWorkFactory(MDCCustomUnitOfWork::new);
		return camelContext;
    }
	
	@Bean
    public CamelBeanPostProcessor camelBeanPostProcessor(CamelContext camelContext, ApplicationContext applicationContext) {
        CamelBeanPostProcessor processor = new CamelBeanPostProcessor();
        processor.setCamelContext(camelContext);
        processor.setApplicationContext(applicationContext);
        return processor;
    }
	
	@Bean
	public ServletRegistrationBean cxfServletRegistrationBean() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new CXFServlet(), contextRoot+"/soap/*");
		registration.setName("CXFServlet");
		return registration;
	}
	
}
