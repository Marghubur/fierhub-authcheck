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
    private Repository repository;
    private EnableDataSourceConfiguration enableDataSourceConfiguration;
    private Authorize authorize;

    public static class EnableDataSourceConfiguration {
        private boolean databaseConfiguration;

        public boolean isDatabaseConfiguration() {
            return databaseConfiguration;
        }

        public void setDatabaseConfiguration(boolean databaseConfiguration) {
            this.databaseConfiguration = databaseConfiguration;
        }
    }

    public static class Repository {
        private String token;
        private String url;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
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

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    // Getters & setters for outer class
    public TokenRequestBody getSecret() {
        return secret;
    }

    public void setSecret(TokenRequestBody secret) {
        this.secret = secret;
    }

    public EnableDataSourceConfiguration getEnable() {
        return enableDataSourceConfiguration;
    }

    public void setEnable(EnableDataSourceConfiguration enableDataSourceConfiguration) {
        this.enableDataSourceConfiguration = enableDataSourceConfiguration;
    }

    public Authorize getAuthorize() {
        return authorize;
    }

    public void setAuthorize(Authorize authorize) {
        this.authorize = authorize;
    }
}