package com.fierhub.configures;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@ConfigurationProperties(
        prefix = "validator"
)
public class RouteValidator {
    private List<String> routes;
    public List<String> openApiEndpoints;
    public Predicate<HttpServletRequest> isSecured = (request) -> {
        return this.openApiEndpoints.stream().anyMatch((uri) -> {
            return request.getRequestURL().toString().contains(uri);
        });
    };

    public RouteValidator() {
    }

    public void setRoutes(List<String> routes) {
        this.routes = routes;
        this.openApiEndpoints = (List)this.routes.stream().map((x) -> {
            return "/" + x;
        }).collect(Collectors.toList());
    }
}