package org.greencheek.dropwtutorial.bootstrap;




import com.sun.jersey.api.client.Client;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.greencheek.dropwtutorial.configuration.HelloWorldConfiguration;
import org.greencheek.dropwtutorial.usage.client.config.UsageClientConfiguration;
import org.greencheek.dropwtutorial.health.TemplateHealthCheck;
import org.greencheek.dropwtutorial.resources.GetJsonDocResource;
import org.greencheek.dropwtutorial.resources.HelloWorldResource;
import org.greencheek.dropwtutorial.service.JerseyGetJsonDoc;
import org.sonatype.spice.jersey.client.ahc.AhcHttpClient;
import org.sonatype.spice.jersey.client.ahc.config.DefaultAhcConfig;



public class HelloWorldApplication extends Application<HelloWorldConfiguration> {
    public static void main(String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(HelloWorldConfiguration configuration,
                    Environment environment) {
        final HelloWorldResource resource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);

        environment.jersey().register(
                new GetJsonDocResource(
                    new JerseyGetJsonDoc(buildNettyPooledClient(configuration, environment), configuration.getJsonDocUrl()),
                    new JerseyGetJsonDoc(buildNettyPooledClient(configuration, environment), configuration.getJsonDocUrl()),
                    new JerseyGetJsonDoc(buildDefaultClient(),configuration.getJsonDocUrl())
                )
        );

    }

    private Client buildNettyPooledClient(HelloWorldConfiguration configuration,
                                          Environment environment) {

        JerseyClientConfiguration jconfig = configuration.getJerseyClientConfiguration();


        DefaultAhcConfig config = new DefaultAhcConfig();

        config.getAsyncHttpClientConfigBuilder().setAllowPoolingConnection(true)
                .setMaximumConnectionsTotal(jconfig.getMaxConnections())
                .setMaximumConnectionsPerHost(jconfig.getMaxConnections())
                .setMaxRequestRetry(jconfig.getRetries())
                .setCompressionEnabled(jconfig.isGzipEnabled())
                .setRequestTimeoutInMs((int) jconfig.getTimeout().toMilliseconds())
                .setMaxConnectionLifeTimeInMs((int) jconfig.getTimeToLive().toMilliseconds())
                .setUserAgent(jconfig.getUserAgent().or("default pooling"));

        return AhcHttpClient.create(config);
//
//        return new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
//                .build(getName());



    }

    private Client buildJerseyPooledClient(HelloWorldConfiguration configuration,
                                          Environment environment) {

        return new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
                .build(getName());



    }

    private Client buildDefaultClient() {
        return Client.create();
    }

}