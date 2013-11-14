package com.inncrewin.waza.test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class TestGet {

	public static void main(String[] args) {
		ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource resource = client.resource("http://localhost:8080/WAZA/rest/user/doLoginUserTest?loginId=test&password=test");
        ClientResponse response = resource.type(MediaType.TEXT_XML).get(ClientResponse.class);
        String en = response.getEntity(String.class);
        System.out.println(en);
	}

}
