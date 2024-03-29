package org.greencheek.dropwtutorial.usage.client.impl.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.yammer.tenacity.core.TenacityCommand;
import org.greencheek.dropwtutorial.configuration.ClientPropertyKeys;
import org.greencheek.dropwtutorial.usage.client.UsageClient;
import org.greencheek.dropwtutorial.usage.client.domain.UsageEvent;

/**
 * Created by dominictootell on 17/08/2014.
 */
public class HystrixLoggingUsageCommand extends TenacityCommand<Void> {

    private final UsageClient client;
    private final UsageEvent event;
    private final UsageClient fallbackClient;

    protected HystrixLoggingUsageCommand(UsageClient client, UsageEvent event,
                                         UsageClient fallback) {
        super(ClientPropertyKeys.INTERNAL_CLIENT);
        this.client = client;
        this.event = event;
        this.fallbackClient = fallback;
    }

    @Override
    protected Void run() throws Exception {
       client.fireUsageEvent(event);
       return null;
    }

    @Override
    protected Void getFallback() {
        fallbackClient.fireUsageEvent(event);
        return null;
    }

}
