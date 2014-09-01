package org.greencheek.dropwtutorial.usage.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.yammer.tenacity.testing.TenacityTest;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.greencheek.dropwtutorial.bootstrap.HelloWorldApplication;
import org.greencheek.dropwtutorial.configuration.*;
import org.greencheek.dropwtutorial.server.EmbeddedTomcatServer;
import org.greencheek.dropwtutorial.server.servlets.ThrowErrorServlet;
import org.greencheek.dropwtutorial.usage.client.FailedToFireUsageEventException;
import org.greencheek.dropwtutorial.usage.client.UsageClient;
import org.greencheek.dropwtutorial.usage.client.config.UsageClientConfiguration;
import org.greencheek.dropwtutorial.usage.client.domain.ArticleViewEvent;
import org.greencheek.dropwtutorial.usage.client.domain.UsageEvent;
import org.greencheek.dropwtutorial.usage.client.factory.ClientFactory;
import org.greencheek.dropwtutorial.usage.client.factory.apache4.JerseyApacheHttpClientFactory;
import org.junit.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by dominictootell on 16/08/2014.
 */
public class TestRetryOfJerseyUsageClient extends TenacityTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private String expectedEventAsString;
    private UsageEvent expectedEvent;
    private UsageClient usageClient;
    EmbeddedTomcatServer server;

    @ClassRule
    public static final DropwizardAppRule<HelloWorldConfiguration> RULE =
            new DropwizardAppRule<HelloWorldConfiguration>(HelloWorldApplication.class, ("target/classes/hello-world.yml"));



    @Before
    public void setUp() throws JsonProcessingException {
        super.testInitialization();
        new HystrixPlugins.UnitTest().reset();
        expectedEvent = new ArticleViewEvent("session1234", "testRetryAndFallBack", "id1234");
        expectedEventAsString = MAPPER.writeValueAsString(expectedEvent);
        server = new EmbeddedTomcatServer();
        server.setupTomcat("/filter");

    }

    @After
    public void tearDown() {
        super.testTeardown();
        server.shutdownTomcat();
        if(usageClient!=null) {
            usageClient.shutdown();
        }
        for(String s : RULE.getEnvironment().metrics().getNames()) {
            RULE.getEnvironment().metrics().remove(s);
        }
        Hystrix.reset(1, TimeUnit.SECONDS);
        ConfigurationManager.getConfigInstance().clear();

        new HystrixPlugins.UnitTest().reset();

    }

//    @Test
//    public void testClientRetry() {
//        int numberOfRetries = 2;
//
//        ThrowErrorServlet throwError = new ThrowErrorServlet(400,numberOfRetries,200,"{ \"ok\": \"ok\"}");
//        String url = server.setupServlet("/throw/*","throwservlet",throwError,false);
//        assertTrue(server.startTomcat());
//        url = server.replacePort(url);
//
//        ClientFactory factory = new JerseyApacheHttpClientFactory();
//        UsageClientConfiguration config = new UsageClientConfiguration();
//        config.setRetries(numberOfRetries);
//        config.setGzipEnabledForRequests(true);
//        config.setUsageEndpoint(url);
//        usageClient = factory.createClient(config,RULE.getEnvironment());
//
//        try {
//            usageClient.fireUsageEvent(expectedEvent);
//        } catch(FailedToFireUsageEventException e) {
//            fail("Should have retried");
//        }
//
//        List<String> sentContent = throwError.getRecievedEntities();
//        assertEquals("Servlet Should have been executed 3 times",3,sentContent.size());
//        for(String s : sentContent) {
//            assertThat(s).isEqualTo(expectedEventAsString);
//        }
//
//    }

//    @Test(expected = FailedToFireUsageEventException.class)
//    public void testFailedToFireUsageEventExceptionIsThrown() {
//        int numberOfRetries = 3;
//
//        ThrowErrorServlet throwError = new ThrowErrorServlet(500,numberOfRetries+1,200,"{ \"ok\": \"ok\"}");
//        String url = server.setupServlet("/throw/*","throwservlet",throwError,false);
//        assertTrue(server.startTomcat());
//        url = server.replacePort(url);
//
//        ClientFactory factory = new JerseyApacheHttpClientFactory();
//        UsageClientConfiguration config = new UsageClientConfiguration();
//        config.setRetries(numberOfRetries);
//        config.setGzipEnabledForRequests(true);
//        config.setUsageEndpoint(url);
//        usageClient = factory.createClient(config,RULE.getEnvironment());
//
//        try {
//            usageClient.fireUsageEvent(expectedEvent);
//        } catch(FailedToFireUsageEventException e) {
//
//            List<String> sentContent = throwError.getRecievedEntities();
//            assertEquals("Servlet Should have been executed "+(numberOfRetries+1)+" times",numberOfRetries+1,sentContent.size());
//            for(String s : sentContent) {
//                assertThat(s).isEqualTo(expectedEventAsString);
//            }
//
//            throw e;
//        }
//
//    }


}
