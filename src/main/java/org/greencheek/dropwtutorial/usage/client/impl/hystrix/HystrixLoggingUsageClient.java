package org.greencheek.dropwtutorial.usage.client.impl.hystrix;

import com.netflix.hystrix.*;
import org.greencheek.dropwtutorial.usage.client.FailedToFireUsageEventException;
import org.greencheek.dropwtutorial.usage.client.UsageClient;
import org.greencheek.dropwtutorial.usage.client.config.UsageClientConfiguration;
import org.greencheek.dropwtutorial.usage.client.domain.UsageEvent;

/**
 * Created by dominictootell on 17/08/2014.
 */
public class HystrixLoggingUsageClient implements UsageClient {

    private final UsageClient wrappedClient;
    private final UsageClient fallbackClient;

    private final HystrixCommand.Setter commandConfiguration;

    public HystrixLoggingUsageClient(UsageClient wrappedClient,
                                     UsageClientConfiguration configuration,
                                     UsageClient fallbackClient){
        this.wrappedClient = wrappedClient;
        this.fallbackClient = fallbackClient;

        HystrixCommand.Setter setter = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(configuration.getHystrixUsageKey() + "Group"));

        setter.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(configuration.getThreadPoolSize())
                .withMaxQueueSize(configuration.getUsageEventQueueSize()));

        setter.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                .withExecutionIsolationThreadTimeoutInMilliseconds((int) (configuration.getRequestTimeout().toMilliseconds())));

        setter.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(configuration.getHystrixUsageKey()));

        commandConfiguration = setter;
    }

    @Override
    public void fireUsageEvent(UsageEvent event) throws FailedToFireUsageEventException {
        new HystrixLoggingUsageCommand(wrappedClient,commandConfiguration,event,fallbackClient).execute();
    }

    @Override
    public void shutdown() {
        wrappedClient.shutdown();
        fallbackClient.shutdown();
    }


}
