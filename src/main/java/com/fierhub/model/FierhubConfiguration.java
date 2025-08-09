package com.fierhub.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.bottomhalf.common.models.TokenRequestBody;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "fierhub")
public class FierhubConfiguration {
    @JsonProperty("Code")
    String code;
    private TokenRequestBody secret;
    private Enable enable;
    private Authorize authorize;

    public static class Enable {
        private boolean databaseConfiguration;

        public boolean isDatabaseConfiguration() {
            return databaseConfiguration;
        }

        public void setDatabaseConfiguration(boolean databaseConfiguration) {
            this.databaseConfiguration = databaseConfiguration;
        }
    }

    public static class Authorize {
        private List<String> urls;

        public List<String> getUrls() {
            return urls;
        }

        public void setUrls(List<String> urls) {
            this.urls = urls;
        }
    }

    // Getters & setters for outer class
    public TokenRequestBody getSecret() {
        return secret;
    }

    public void setSecret(TokenRequestBody secret) {
        this.secret = secret;
    }

    public Enable getEnable() {
        return enable;
    }

    public void setEnable(Enable enable) {
        this.enable = enable;
    }

    public Authorize getAuthorize() {
        return authorize;
    }

    public void setAuthorize(Authorize authorize) {
        this.authorize = authorize;
    }
}