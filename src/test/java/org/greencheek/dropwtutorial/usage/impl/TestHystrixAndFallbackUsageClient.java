package org.greencheek.dropwtutorial.usage.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.util.Duration;
import org.greencheek.dropwtutorial.bootstrap.HelloWorldApplication;
import org.greencheek.dropwtutorial.configuration.HelloWorldConfiguration;
import org.greencheek.dropwtutorial.server.EmbeddedTomcatServer;
import org.greencheek.dropwtutorial.server.servlets.ThrowErrorServlet;
import org.greencheek.dropwtutorial.usage.client.FailedToFireUsageEventException;
import org.greencheek.dropwtutorial.usage.client.UsageClient;
import org.greencheek.dropwtutorial.usage.client.config.UsageClientConfiguration;
import org.greencheek.dropwtutorial.usage.client.domain.ArticleViewEvent;
import org.greencheek.dropwtutorial.usage.client.domain.UsageEvent;
import org.greencheek.dropwtutorial.usage.client.factory.ClientFactory;
import org.greencheek.dropwtutorial.usage.client.factory.apache4.JerseyApacheHttpClientFactory;
import org.greencheek.dropwtutorial.usage.client.factory.hystrix.HystrixJerseyApacheWithLoggingClientFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * Created by dominictootell on 16/08/2014.
 */
public class TestHystrixAndFallbackUsageClient {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private String expectedEventAsString;
    private UsageEvent expectedEvent;
    private UsageClient usageClient;
    private UsageClient fallbackClient;
    EmbeddedTomcatServer server;

    @ClassRule
    public static final DropwizardAppRule<HelloWorldConfiguration> RULE =
            new DropwizardAppRule<HelloWorldConfiguration>(HelloWorldApplication.class, ("target/classes/hello-world.yml"));



    @Before
    public void setUp() throws JsonProcessingException {
        expectedEvent = new ArticleViewEvent("session1234", "testRetryAndFallBack", "id1234");
        expectedEventAsString = MAPPER.writeValueAsString(expectedEvent);
        server = new EmbeddedTomcatServer();
        server.setupTomcat("/filter");

    }

    @After
    public void tearDown() {
        server.shutdownTomcat();
        if(usageClient!=null) {
            usageClient.shutdown();
        }

        if(fallbackClient!=null) {
            fallbackClient.shutdown();
        }

        for(String s : RULE.getEnvironment().metrics().getNames()) {
            RULE.getEnvironment().metrics().remove(s);
        }
    }


    @Test
    public void testFallbackIsCalledAfterRetriesFail() {
        int numberOfRetries = 3;
        ThrowErrorServlet throwError = new ThrowErrorServlet(500,numberOfRetries+1,200,"{ \"ok\": \"ok\"}");
        String url = server.setupServlet("/throw/*","throwservlet",throwError,false);
        assertTrue(server.startTomcat());
        url = server.replacePort(url);

        final AtomicInteger fallbackCalled = new AtomicInteger(0);

        fallbackClient = new UsageClient() {
            @Override
            public void fireUsageEvent(UsageEvent event) throws FailedToFireUsageEventException {
                fallbackCalled.incrementAndGet();
            }

            @Override
            public void shutdown() {

            }
        };

        ClientFactory factory = new HystrixJerseyApacheWithLoggingClientFactory(fallbackClient);
        UsageClientConfiguration config = new UsageClientConfiguration();
        config.setRetries(numberOfRetries);
        config.setGzipEnabledForRequests(true);
        config.setConnectionTimeout(Duration.milliseconds(2000));
        config.setRequestTimeout(Duration.milliseconds(3000));
        config.setUsageEndpoint(url);
        usageClient = factory.createClient(config,RULE.getEnvironment());

        usageClient.fireUsageEvent(expectedEvent);

        List<String> sentContent = throwError.getRecievedEntities();
        assertEquals("Servlet Should have been executed " + (numberOfRetries + 1) + " times", numberOfRetries + 1, sentContent.size());
        for (String s : sentContent) {
            assertThat(s).isEqualTo(expectedEventAsString);
        }

        assertEquals("Fallback should have been called",1,fallbackCalled.get());


    }


}
