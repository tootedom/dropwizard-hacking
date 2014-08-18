package org.greencheek.dropwtutorial.usage.client;

import org.greencheek.dropwtutorial.usage.client.domain.UsageEvent;

/**
 * Created by dominictootell on 17/08/2014.
 */
public interface UsageClient {

    public void fireUsageEvent(UsageEvent event) throws FailedToFireUsageEventException;
    public void shutdown();
}
