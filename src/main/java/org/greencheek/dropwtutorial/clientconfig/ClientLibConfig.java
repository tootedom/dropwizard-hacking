package org.greencheek.dropwtutorial.clientconfig;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.tenacity.core.config.CircuitBreakerConfiguration;
import com.yammer.tenacity.core.config.TenacityConfiguration;
import com.yammer.tenacity.core.config.ThreadPoolConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by dominictootell on 31/08/2014.
 */
public class ClientLibConfig {

    @Valid
    @NotNull
    @JsonProperty
    private TenacityConfiguration clientLibCommand = new TenacityConfiguration(
            new ThreadPoolConfiguration(10,10,Integer.MAX_VALUE,10,10000,1000),
            new CircuitBreakerConfiguration(100,100,100,10000,1000),
           10000
    );

    @JsonProperty
    public void setClientLibCommand(TenacityConfiguration config) {
        this.clientLibCommand = config;
    }

    public TenacityConfiguration getClientLibCommand() {
        return clientLibCommand;
    }
}
