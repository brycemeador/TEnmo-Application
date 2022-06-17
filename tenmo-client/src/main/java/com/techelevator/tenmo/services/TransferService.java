package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser authenticatedUser;
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public void setAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public AuthenticatedUser user(){
        AuthenticatedUser user = new AuthenticatedUser();
        setToken(user.getToken());
        setAuthenticatedUser(user);
        return user;
    }

    public Transfer[] transferHistory(int accountId, AuthenticatedUser user){
        Transfer[] transfers = null;
        setToken(user.getToken());
        setAuthenticatedUser(user);
        try{
            transfers = restTemplate.exchange(API_BASE_URL + "/transfer/history/" + accountId, HttpMethod.GET,
                    makeAuthEntity(), Transfer[].class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
        }
        return transfers;
    }

    public Transfer transfer(int accountId, AuthenticatedUser user){
        Transfer transfer = null;
        setToken(user.getToken());
        setAuthenticatedUser(user);
        try {
            transfer = restTemplate.exchange(API_BASE_URL + "/transfer/" + accountId + "/" + accountId,
                    HttpMethod.POST, makeAuthEntity(), Transfer.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
        }
        return transfer;
    }




    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
}
