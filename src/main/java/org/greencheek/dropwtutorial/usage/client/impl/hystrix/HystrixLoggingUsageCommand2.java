package org.greencheek.dropwtutorial.usage.client.impl.hystrix;

import com.yammer.tenacity.core.TenacityCommand;
import org.greencheek.dropwtutorial.clientconfig.ClientLibPropertyKeys;
import org.greencheek.dropwtutorial.configuration.ClientPropertyKeys;
import org.greencheek.dropwtutorial.usage.client.UsageClient;
import org.greencheek.dropwtutorial.usage.client.domain.UsageEvent;

/**
 * Created by dominictootell on 17/08/2014.
 */
public class HystrixLoggingUsageCommand2 extends TenacityCommand<Void> {

    private final UsageClient client;
    private final UsageEvent event;
    private final UsageClient fallbackClient;

    protected HystrixLoggingUsageCommand2(UsageClient client, UsageEvent event,
                                          UsageClient fallback) {
        super(ClientLibPropertyKeys.CLIENT_LIB);
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
