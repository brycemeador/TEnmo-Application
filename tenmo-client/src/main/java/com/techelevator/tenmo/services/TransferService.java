package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser authenticatedUser;
    private String token;
    private User user;

    public void setToken(String token) {
        this.token = token;
    }

    public void setAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    //Grabs users past transfers from the server
    public Transfer[] transferHistory(int accountId, AuthenticatedUser user){
        Transfer[] transfers = null;
        setToken(user.getToken());
        setAuthenticatedUser(user);
        try{
            transfers = restTemplate.exchange(API_BASE_URL + "/transfer/history/" + accountId, HttpMethod.GET,
                    makeAuthEntity(), Transfer[].class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    //Pushes transfer to the server
    public Boolean addTransfer(Transfer transfer, int accountFromId, int accountToId, AuthenticatedUser authenticatedUser) throws RestClientResponseException, ResourceAccessException {
        Boolean transferResponse = false;
        setToken(authenticatedUser.getToken());
        setAuthenticatedUser(authenticatedUser);
        try {
            transferResponse = restTemplate.exchange(API_BASE_URL + "/transfer/" +
                            accountFromId + "/" + accountToId,
                    HttpMethod.POST,
                    makeTransferEntity(transfer),
                    Boolean.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferResponse;
    }

    //Grabs the detail of a specific transfer
    public Transfer transferDetails(int transferId) throws RestClientResponseException, ResourceAccessException {
        Transfer transfer = null;
        try {
            transfer = restTemplate.exchange(API_BASE_URL + "/transferdetails/" + transferId, HttpMethod.GET,
                    makeAuthEntity(), Transfer.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    //List all users
    public User[] listUsers(AuthenticatedUser authenticatedUser){
        setToken(authenticatedUser.getToken());
        setAuthenticatedUser(authenticatedUser);
        User [] users = null;
        try {
            users = restTemplate.exchange(API_BASE_URL + "/user",
                    HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return users;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
}
