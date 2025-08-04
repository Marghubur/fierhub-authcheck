package com.fierhub.model;

import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.security.InvalidKeyException;
import java.util.List;
import java.util.Map;

@Component
@RequestScope(
        proxyMode = ScopedProxyMode.TARGET_CLASS
)
public class CurrentSession {
    String email;
    Integer userId;
    String code;
    boolean expired;
    Map<String, Object> claimsValue;
    List<GrantedAuthority> roles;

    public Map<String, Object> getClaimsValue() {
        return this.claimsValue;
    }

    public void setClaimsValue(Map<String, Object> claimsValue) {
        this.claimsValue = claimsValue;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isExpired() {
        return this.expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public List<GrantedAuthority> getRoles() {
        return this.roles;
    }

    public void setRoles(List<GrantedAuthority> roles) {
        this.roles = roles;
    }

    public CurrentSession() {
    }

    public CurrentSession(String email, Integer userId, String code, boolean expired, Map<String, Object> claimsValue, List<GrantedAuthority> roles) {
        this.email = email;
        this.userId = userId;
        this.code = code;
        this.expired = expired;
        this.claimsValue = claimsValue;
        this.roles = roles;
    }

    public String toString() {
        return "CurrentSession{email='" + this.email + "', userId=" + this.userId + ", code='" + this.code + "', expired=" + this.expired + ", claimsValue=" + this.claimsValue + ", roles=" + this.roles + "}";
    }

    public <T> Class<T> getClaimValue(String key) throws Exception {
        if (this.claimsValue.containsKey(key)) {
            Object value = this.claimsValue.get(key);
            return (Class<T>) value.getClass();
        } else {
            throw new InvalidKeyException("Field " + key + " not found in Current token session");
        }
    }
}

