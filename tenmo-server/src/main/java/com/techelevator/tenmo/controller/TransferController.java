package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    @Autowired
    private TransferDao transferDao;

    @GetMapping("account/transfer/{id}")
    public List<Transfer> getAllTransfers(@PathVariable int id) {
        List<Transfer> output = transferDao.getAllTransfers(id);
        return output;
    }

    //TODO Get transfer by ID

    @PostMapping("transfer")
    public String sendTransferRequest(@RequestBody Transfer transfer) {
        String results = transferDao.sendTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
        return results;
    }

    @PostMapping("request")
    public String requestTransferRequest(@RequestBody Transfer transfer) {
        String results = transferDao.requestTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
        return results;
    }

    //TODO Get all transfer requests

    //TODO update requests
}
