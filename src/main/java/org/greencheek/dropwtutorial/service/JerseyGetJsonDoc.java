package org.greencheek.dropwtutorial.service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;

/**
 * Created by dominictootell on 16/08/2014.
 */
public class JerseyGetJsonDoc implements GetJsonDoc {

    private final Client jerseyClient;
    private final String url;

    public JerseyGetJsonDoc(Client client, String url) {
        this.jerseyClient = client;
        this.url = url;
    }

    @Override
    public String get() {
        WebResource webResource = this.jerseyClient.resource(this.url);
        WebResource.Builder builder = webResource.type(MediaType.APPLICATION_JSON);
        builder.accept(MediaType.APPLICATION_JSON);

        return builder.get(String.class);
    }
}
