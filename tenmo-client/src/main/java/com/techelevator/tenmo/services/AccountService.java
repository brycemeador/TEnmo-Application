package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser authenticatedUser;
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    //Grabs account ID from server using the user ID
    public Integer getAccountId(int userId, AuthenticatedUser user) {
        setToken(user.getToken());
        setAuthenticatedUser(user);
        Integer accountId = null;
        try {
            accountId = restTemplate.exchange(API_BASE_URL + "user/" + userId,
                    HttpMethod.GET,
                    makeAuthEntity(), Integer.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return accountId;
    }

    //Grabs the username from the server using the user ID
    public String getUsername(int userId, AuthenticatedUser user) {
        setToken(user.getToken());
        setAuthenticatedUser(user);
        String username = null;
        try {
            username = restTemplate.exchange(API_BASE_URL + "username/" + userId,
                    HttpMethod.GET,
                    makeAuthEntity(), String.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return username;
    }

    //Grabs the user ID from the server using the account ID
    public String getUserId(int accountId, AuthenticatedUser user) {
        setToken(user.getToken());
        setAuthenticatedUser(user);
        String username = null;
        try {
            username = restTemplate.exchange(API_BASE_URL + "accountId/" + accountId,
                    HttpMethod.GET,
                    makeAuthEntity(), String.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return username;
    }

    //Grabs the user's balance from the server using the user ID
    public BigDecimal getBalance(AuthenticatedUser user) throws RestClientResponseException, ResourceAccessException {
        BigDecimal balance = null;
        setToken(user.getToken());
        setAuthenticatedUser(user);
        try {
            balance = restTemplate.exchange(API_BASE_URL + "account/balance/" + user.getUser().getId(),
                    HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public void setAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
}
