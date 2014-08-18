package org.greencheek.dropwtutorial.usage.client;

import org.greencheek.dropwtutorial.usage.client.domain.UsageEvent;

/**
 * Created by dominictootell on 17/08/2014.
 */
public class FailedToFireUsageEventException extends RuntimeException {

    private final UsageEvent eventAttemptedToBeFired;

    public FailedToFireUsageEventException(UsageEvent event) {
        super();
        eventAttemptedToBeFired = event;
    }

    @Override
    public Throwable fillInStackTrace()
    {
        return this;
    }
}
