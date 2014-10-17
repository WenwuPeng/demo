package com.demo.chaos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Configuration;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.internal.metadata.raw.BeanConfiguration;

public class ChaosConfiguration extends Configuration {
    @NotEmpty
    @JsonProperty
    private String template;

    @NotEmpty
    @JsonProperty
    private String defaultName;

    @JsonProperty
    private String ip;

    public String getTemplate() {
        return template;
    }
    public String getDefaultName() {
        return defaultName;
    }

    public String getIp() {
        return ip;
    }
}