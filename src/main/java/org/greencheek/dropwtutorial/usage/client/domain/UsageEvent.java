package org.greencheek.dropwtutorial.usage.client.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Created by dominictootell on 17/08/2014.
 */
public abstract class UsageEvent {

    private String sessionId;
    private String appName;
    private String correlationId;
    private String createdAt;

    public UsageEvent(String sessionId, String appName, String correlationId) {
        this.sessionId = sessionId;
        this.appName = appName;
        this.correlationId = correlationId;
        createdAt = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC")).format(Instant.now(((Clock.systemUTC()))));
//                LocalDateTime.now(Clock.systemUTC()).toLocalDate().format(DateTimeFormatter.ISO_INSTANT);

    }

    @JsonProperty
    public String getSessionId() {
        return sessionId;
    }

    @JsonProperty
    public void setSessionId(String text) {
        this.sessionId = text;
    }

    @JsonProperty
    public String getAppName() {
        return appName;
    }

    @JsonProperty
    public void setAppName(String text) {
        this.appName = text;
    }

    @JsonProperty
    public String getCorrelationId() {
        return sessionId;
    }

    @JsonProperty
    public void setCorrelationId(String text) {
        this.sessionId = text;
    }

    @JsonProperty
    public String getCreatedAt() {
        return this.createdAt;
    }

    @JsonProperty
    public void setCreatedAt(String date) {
        this.createdAt = date;
    }


}
