package org.greencheek.dropwtutorial.usage.client.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by dominictootell on 17/08/2014.
 */
public class ArticleViewEvent extends UsageEvent {


    public ArticleViewEvent(String sessionId, String appName, String correlationId) {
        super(sessionId, appName, correlationId);
    }
}
