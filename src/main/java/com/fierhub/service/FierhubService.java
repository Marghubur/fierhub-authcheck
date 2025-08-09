package com.fierhub.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.bottomhalf.common.database.DataSourceProperties;
import in.bottomhalf.common.database.DatabaseProperties;
import in.bottomhalf.common.models.ApiResponse;
import in.bottomhalf.common.models.FierhubTokeResponse;
import in.bottomhalf.common.models.TokenRequestBody;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;


@Service
public class FierhubService {
//    @Value("${fierhub.repository.tokenRepositoryUrl:#{null}}")
//    public String tokenRepositoryUrl;
//    @Value("${fierhub.repository.token:#{null}}")
//    public String accessToken;
    @Autowired
    DataSourceProperties getCurrentDataSource;
    @Autowired
    DatabaseProperties dbConfig;
    @Autowired
    ObjectMapper mapper;
//    @Autowired
//    TokenRequestBody tokenRequestBody;

    public FierhubService() {
    }

//    private void readTokenConfigurationFile() throws Exception {
//        if (this.tokenRepositoryUrl != null && !this.tokenRepositoryUrl.isEmpty()) {
//            String requestBody = "{\"accessToken\": \"" + this.accessToken + "\"}";
//            String resultContent = this.postRequest(requestBody, this.tokenRepositoryUrl);
//            FierhubTokeResponse fierhubTokeResponse = (FierhubTokeResponse)this.mapper.readValue(resultContent, FierhubTokeResponse.class);
//            if (fierhubTokeResponse.statusCode != HttpStatus.OK.value()) {
//                throw new Exception("Fail to get the Fierhub token repository detail. Please check your configuration.");
//            } else {
//                TokenRequestBody tokenRequestBody = (TokenRequestBody)this.mapper.readValue(fierhubTokeResponse.responseBody, TokenRequestBody.class);
//                if (tokenRequestBody != null) {
//                    this.tokenRequestBody.setClaims(tokenRequestBody.getClaims());
//                    this.tokenRequestBody.setCompanyCode(tokenRequestBody.getCompanyCode());
//                    this.tokenRequestBody.setKey(tokenRequestBody.getKey());
//                    this.tokenRequestBody.setIssuer(tokenRequestBody.getIssuer());
//                    this.tokenRequestBody.setExpiryTimeInSeconds(tokenRequestBody.getExpiryTimeInSeconds());
//                    this.tokenRequestBody.setRefreshTokenExpiryTimeInSeconds(tokenRequestBody.getRefreshTokenExpiryTimeInSeconds());
//                }
//
//            }
//        } else {
//            throw new Exception("Please add fierhub.repository.url in your application.yml(.properties) file");
//        }
//    }
//
//    @PostConstruct
//    public void init() throws Exception {
//        // this.readTokenConfigurationFile();
//    }

    public void configureConnection() throws Exception {
        String company = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("Code");
        DatabaseProperties config = this.getCurrentDataSource.getConfig(company);
        this.dbConfig.setPassword(config.getPassword());
        this.dbConfig.setUsername(config.getUsername());
        this.dbConfig.setJdbcUrl(config.getJdbcUrl());
        this.dbConfig.setDriverClassName(config.getDriverClassName());
        this.dbConfig.setDatabase(config.getDatabase());
    }

    public ApiResponse generateToken(TokenRequestBody requestBody) throws Exception {
        // TokenRequestBody requestBody = this.tokenRequestBody.getTokenRequest(claims);
        String response = this.postRequest(this.mapper.writeValueAsString(requestBody), "https://www.bottomhalf.in/bt/s3/ExternalTokenManager/generateToken");
        ApiResponse apiResponse = (ApiResponse)this.mapper.readValue(response, ApiResponse.class);
        return apiResponse;
    }

    private String postRequest(String requestBody, String filePath) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(filePath)).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream)client.send(request, HttpResponse.BodyHandlers.ofInputStream()).body()));

            String var8;
            try {
                StringBuilder content = new StringBuilder();

                while(true) {
                    String line;
                    if ((line = reader.readLine()) == null) {
                        var8 = content.toString();
                        break;
                    }

                    content.append(line).append("\n");
                }
            } catch (Throwable var10) {
                try {
                    reader.close();
                } catch (Throwable var9) {
                    var10.addSuppressed(var9);
                }

                throw var10;
            }

            reader.close();
            return var8;
        } catch (Exception var11) {
            throw new Exception(var11.getMessage());
        }
    }
}

