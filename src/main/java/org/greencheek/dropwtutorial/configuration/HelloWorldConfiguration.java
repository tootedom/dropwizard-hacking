package org.greencheek.dropwtutorial.configuration;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.client.JerseyClientConfiguration;
import org.greencheek.dropwtutorial.usage.client.config.UsageClientConfiguration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class HelloWorldConfiguration extends Configuration {
    @NotEmpty
    private String template;

    @NotEmpty
    private String defaultName = "Stranger";

    @JsonProperty
    public String getTemplate() {
        return template;
    }

    @JsonProperty
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty
    public void setDefaultName(String name) {
        this.defaultName = name;
    }




    @NotEmpty
    private String jsonDocUrl;

    @JsonProperty
    public String getJsonDocUrl() {
        return jsonDocUrl;
    }

    @Valid
    @NotNull
    private UsageClientConfiguration usageClientConfiguration;

    @JsonProperty
    public UsageClientConfiguration getUsageClientConfiguration() {
        return usageClientConfiguration;
    }

    @JsonProperty
    public void setUsageClientConfiguration(UsageClientConfiguration config) {
        usageClientConfiguration = config;
    }



    @Valid
    @NotNull
    private JerseyClientConfiguration jerseyClientConfiguration;

    @JsonProperty
    public void setJerseyClientConfiguration(JerseyClientConfiguration config) {
        this.jerseyClientConfiguration = config;
    }

    @JsonProperty
    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return jerseyClientConfiguration;
    }

}