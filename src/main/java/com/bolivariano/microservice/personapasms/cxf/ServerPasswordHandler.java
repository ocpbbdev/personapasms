package com.bolivariano.microservice.personapasms.cxf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * Callback handler to handle passwords
 */
@Service
public class ServerPasswordHandler implements CallbackHandler {

	@Value("${server.security.encrypUsername}")
	String encrypUsername;
	@Value("${server.security.encrypPassword}")
	String encrypPassword;
	
	@Value("${client.service.camel.username}")
	String username;
	@Value("${client.service.camel.password}")
	String password;
	
	
	
    private Map<String, String> passwords = new HashMap<String, String>();

    /**
     * Here, we attempt to get the password from the private
     * alias/passwords map.
     */
    @PostConstruct
    public void initServerPasswordHandler() {
    	passwords.put(encrypUsername, encrypPassword);
    	passwords.put(username, password);
    }
    
    
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            WSPasswordCallback pc = (WSPasswordCallback)callbacks[i];

            String pass = passwords.get(pc.getIdentifier());
            if (pass != null) {
                pc.setPassword(pass);
                return;
            }
        }
    }

    /**
     * Add an alias/password pair to the callback mechanism.
     */
    public void setAliasPassword(String alias, String password) {
        passwords.put(alias, password);
    }
}