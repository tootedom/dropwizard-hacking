package org.greencheek.dropwtutorial.configuration;

import com.google.common.collect.ImmutableMap;
import com.yammer.tenacity.core.config.BreakerboxConfiguration;
import com.yammer.tenacity.core.config.TenacityConfiguration;
import com.yammer.tenacity.core.properties.TenacityPropertyKey;
import com.yammer.tenacity.core.properties.TenacityPropertyRegister;
import io.dropwizard.util.Duration;
import org.greencheek.dropwtutorial.clientconfig.ClientLibPropertyKeys;

/**
 * Created by dominictootell on 31/08/2014.
 */
public class ClientPropertyRegister {
    private final HelloWorldConfiguration configuration;

    public ClientPropertyRegister(HelloWorldConfiguration configuration) {
        this.configuration = configuration;
    }

    public void register() {
        final ImmutableMap.Builder<TenacityPropertyKey, TenacityConfiguration> builder = ImmutableMap.builder();

        builder.put(ClientPropertyKeys.INTERNAL_CLIENT, configuration.getClientCommand());
        builder.put(ClientLibPropertyKeys.CLIENT_LIB, configuration.getClientLibConfig().getClientLibCommand());


        new TenacityPropertyRegister(builder.build(), new BreakerboxConfiguration("http://localhost:8080/archaius/breakerbox",
                Duration.seconds(0),
                Duration.seconds(10))).register();

    }
}
