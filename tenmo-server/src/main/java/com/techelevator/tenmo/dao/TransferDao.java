package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    public List<Transfer> getAllTransfers(int userId);
    public Transfer getTransferById(int transferId);
    public String sendTransfer(int userFrom, int userTo, BigDecimal amount);
    public String requestTransfer(int userFrom, int userTo, BigDecimal amount);
    public List<Transfer> getPendingTransfers(int userId);

    List<Transfer> getPendingRequests(int userId);

    public String updateTransferRequest(Transfer transfer, int statusId);
}
