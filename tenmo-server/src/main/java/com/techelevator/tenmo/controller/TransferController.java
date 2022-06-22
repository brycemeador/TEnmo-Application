package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private TransferDao dao;

    public TransferController(JdbcTransferDao dao) {
        this.dao = dao;
    }

    ////Path to get the user's transfer history
    @GetMapping("/transfer/history/{id}")
    public List<Transfer> transferHistory(@PathVariable Long id) {
        return dao.transferHistory(id);
    }

    //Path to execute transfer between accounts
    @PostMapping("/transfer/{accountFromId}/{accountToId}")
    public boolean transfer(@RequestBody Transfer transfer, @PathVariable int accountFromId, @PathVariable int accountToId) throws Exception {
        return dao.transfer(transfer, accountFromId, accountToId);
    }

    //Path to get the transfer details of a prior transfer
    @GetMapping("/transferdetails/{transferId}")
    public Transfer transferDetails(@PathVariable int transferId){
        return dao.transferDetails(transferId);
    }
}