package org.greencheek.dropwtutorial.usage.client.impl;

import org.greencheek.dropwtutorial.usage.client.FailedToFireUsageEventException;
import org.greencheek.dropwtutorial.usage.client.UsageClient;
import org.greencheek.dropwtutorial.usage.client.domain.UsageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dominictootell on 17/08/2014.
 */
public class Slf4jLoggingUsageClient implements UsageClient {

    public final static Slf4jLoggingUsageClient INSTANCE = new Slf4jLoggingUsageClient();

    private final static Logger logger = LoggerFactory.getLogger(Slf4jLoggingUsageClient.class);

    @Override
    public void fireUsageEvent(UsageEvent event) throws FailedToFireUsageEventException {
        logger.error(event.toString());
    }

    @Override
    public void shutdown() {

    }
}
