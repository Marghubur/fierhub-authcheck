package com.fierhub.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fierhub.model.CurrentSession;
import in.bottomhalf.common.models.TokenRequestBody;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JwtService {
    @Autowired
    TokenRequestBody tokenRequestBody;
    @Autowired
    CurrentSession currentSession;
    @Autowired
    ObjectMapper mapper;

    public JwtService() {
    }

    @Bean
    public JwtService jwtService() {
        return new JwtService();
    }

    public Key getSignInKey() {
        return Keys.hmacShaKeyFor(this.tokenRequestBody.getKey().getBytes());
    }

    public String extractUsername(String token) {
        return (String)this.extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return (Date)this.extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public List<GrantedAuthority> extractAuthorities(String token) {
        String result = (String)this.extractClaim(token, (x) -> {
            return (String)x.get("roles", String.class);
        });
        return this.convertToAuthorities(result);
    }

    public List<GrantedAuthority> convertToAuthorities(String result) {
        List<String> roles = (List)this.mapper.convertValue(List.of(result.split(",")), new TypeReference<List<String>>() {
        });
        return (List)roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public CurrentSession getCurrentSession(String token) {
        Claims claims = this.extractAllClaims(token);
        this.currentSession.setEmail((String)claims.get("email", String.class));
        this.currentSession.setUserId(Integer.parseInt((String)claims.get("userId", String.class)));
        this.currentSession.setCode((String)claims.get("company", String.class));
        String authorities = (String)claims.get("roles", String.class);
        this.currentSession.setRoles(this.convertToAuthorities(authorities));
        String expiration = (String)claims.get("expiration", String.class);
        this.currentSession.setExpired(Instant.parse(expiration).compareTo(Instant.now()) < 0);
        return this.currentSession;
    }

    public Claims extractAllClaims(String token) {
        return (Claims) Jwts.parserBuilder().setSigningKey(this.getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    public boolean isTokenExpired(String token) {
        return this.extractExpiration(token).before(new Date());
    }

    public String generateToken(String username, Map<String, Object> extraClaims) {
        return Jwts.builder().setClaims(extraClaims).setSubject(username).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + (long)this.tokenRequestBody.getExpiryTimeInSeconds())).signWith(this.getSignInKey(), SignatureAlgorithm.HS256).compact();
    }
}

