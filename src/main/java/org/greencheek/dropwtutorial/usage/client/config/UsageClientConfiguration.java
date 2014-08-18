package org.greencheek.dropwtutorial.usage.client.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.util.Duration;
import io.dropwizard.validation.ValidationMethod;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * Created by dominictootell on 16/08/2014.
 */
public class UsageClientConfiguration {


    @NotBlank
    private String hystrixUsageKey = "UsageEvents";

    @NotBlank
    private String usageEndpoint;

    private boolean gzipEnabled = true;

    private boolean gzipEnabledForRequests = true;

    @Min(1)
    @Max(Integer.MAX_VALUE)
    private int usageEventQueueSize = 10240;

    @Min(1)
    @Max(100)
    private int threadPoolSize = 20;

    @NotNull
    private Duration requestTimeout = Duration.milliseconds(500);

    @NotNull
    private Duration connectionTimeout = Duration.milliseconds(500);

    @NotNull
    private Duration timeToLive = Duration.hours(1);

    private boolean cookiesEnabled = false;

    @NotNull
    private Duration keepAlive = Duration.milliseconds(0);

    @Min(0)
    @Max(1000)
    private int retries = 0;

    @NotNull
    private Optional<String> userAgent = Optional.absent();

    @JsonProperty
    public UsageClientConfiguration setHystrixUsageKey(String key) {
        hystrixUsageKey = key;
        return this;
    }

    @JsonProperty
    public String getHystrixUsageKey() {
        return hystrixUsageKey;
    }

    @JsonProperty
    public UsageClientConfiguration setUsageEndpoint(String url) {
        this.usageEndpoint = url;
        return this;
    }

    @JsonProperty
    public String getUsageEndpoint() {
        return this.usageEndpoint;
    }

    @JsonProperty
    public Duration getKeepAlive() {
        return keepAlive;
    }

    @JsonProperty
    public UsageClientConfiguration setKeepAlive(Duration keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }

    @JsonProperty
    public Duration getRequestTimeout() {
        return requestTimeout;
    }

    @JsonProperty
    public Duration getConnectionTimeout() {
        return connectionTimeout;
    }

    @JsonProperty
    public Duration getTimeToLive() {
        return timeToLive;
    }

    @JsonProperty
    public boolean isCookiesEnabled() {
        return cookiesEnabled;
    }

    @JsonProperty
    public UsageClientConfiguration setRequestTimeout(Duration duration) {
        this.requestTimeout = duration;
        return this;
    }

    @JsonProperty
    public UsageClientConfiguration setConnectionTimeout(Duration duration) {
        this.connectionTimeout = duration;
        return this;
    }

    @JsonProperty
    public UsageClientConfiguration setTimeToLive(Duration timeToLive) {
        this.timeToLive = timeToLive;
        return this;
    }

    @JsonProperty
    public UsageClientConfiguration setCookiesEnabled(boolean enabled) {
        this.cookiesEnabled = enabled;
        return this;
    }


    @JsonProperty
    public int getRetries() {
        return retries;
    }

    @JsonProperty
    public UsageClientConfiguration setRetries(int retries) {
        this.retries = retries;
        return this;
    }

    @JsonProperty
    public Optional<String> getUserAgent() {
        return userAgent;
    }

    @JsonProperty
    public UsageClientConfiguration setUserAgent(Optional<String> userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    @JsonProperty
    public UsageClientConfiguration setUsageEventQueueSize(int size) {
        this.usageEventQueueSize = size;
        return this;
    }

    @JsonProperty
    public int getUsageEventQueueSize() {
        return usageEventQueueSize;
    }

    @JsonProperty
    public UsageClientConfiguration setThreadPoolSize(int size) {
        this.threadPoolSize = size;
        return this;
    }

    @JsonProperty
    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    @JsonProperty
    public boolean isGzipEnabled() {
        return gzipEnabled;
    }

    @JsonProperty
    public UsageClientConfiguration setGzipEnabled(boolean enabled) {
        this.gzipEnabled = enabled;
        return this;
    }

    @JsonProperty
    public boolean isGzipEnabledForRequests() {
        return gzipEnabledForRequests;
    }

    @JsonProperty
    public UsageClientConfiguration setGzipEnabledForRequests(boolean enabled) {
        this.gzipEnabledForRequests = enabled;
        return this;
    }

    @JsonIgnore
    @ValidationMethod(message = ".gzipEnabledForRequests requires gzipEnabled set to true")
    public boolean isCompressionConfigurationValid() {
        return !gzipEnabledForRequests || gzipEnabled;
    }


    public JerseyClientConfiguration toJerseyClientConfiguration() {
        JerseyClientConfiguration config = new JerseyClientConfiguration();
        config.setGzipEnabled(this.isGzipEnabled());
        config.setGzipEnabledForRequests(this.isGzipEnabledForRequests());
        config.setMaxThreads(this.getThreadPoolSize());
        config.setMinThreads(this.getThreadPoolSize());
        config.setConnectionTimeout(this.getConnectionTimeout());
        config.setTimeout(this.getRequestTimeout());
        config.setTimeToLive(this.getTimeToLive());
        config.setCookiesEnabled(this.isCookiesEnabled());
        config.setMaxConnections(this.getThreadPoolSize());
        config.setMaxConnectionsPerRoute(this.getThreadPoolSize());
        config.setRetries(0);
        config.setKeepAlive(this.getKeepAlive());
        config.setUserAgent(this.getUserAgent());
        return config;
    }

}
