package org.greencheek.dropwtutorial.usage.client.impl;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.greencheek.dropwtutorial.usage.client.FailedToFireUsageEventException;
import org.greencheek.dropwtutorial.usage.client.UsageClient;
import org.greencheek.dropwtutorial.usage.client.config.UsageClientConfiguration;
import org.greencheek.dropwtutorial.usage.client.domain.UsageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.net.URI;

/**
 * Created by dominictootell on 17/08/2014.
 */
public class JerseyUsageClient implements UsageClient {

    private final Client jerseyClient;
    private final URI usageEndpoint;
    private final int retries;
    private final static Logger logger = LoggerFactory.getLogger(JerseyUsageClient.class);


    public JerseyUsageClient(Client c, UsageClientConfiguration configuration) {
        jerseyClient = c;
        usageEndpoint = URI.create(configuration.getUsageEndpoint());
        retries = configuration.getRetries();
    }

    @Override
    public void fireUsageEvent(UsageEvent event) throws FailedToFireUsageEventException {
        sendUsageEvent(event,retries);
    }

    @Override
    public void shutdown() {
        jerseyClient.destroy();
    }

    private void sendUsageEvent(UsageEvent event, int retries)  {
        for(int i = 0;i<=retries;i++) {

            WebResource resource = jerseyClient.resource(usageEndpoint);

            WebResource.Builder builder = resource.type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(event,MediaType.APPLICATION_JSON_TYPE);

            ClientResponse response = null;
            try {

                response = builder.put(ClientResponse.class);
                if(response.getStatus()==200) {
                    return;
                }
            } catch (Throwable e ) {
                e.printStackTrace();
                if(i<retries) {
                    logger.error("Unable to send usage event, retry will take place",e);
                } else {
                    logger.error("Unable to send usage event, all retries have occurred, exception will be raised",e);
                }
            } finally {
                if(response!=null) {
                    try {
                        response.close();
                    } catch (ClientHandlerException e) {

                    }
                }
            }
        }

        throw new FailedToFireUsageEventException(event);
    }

}
