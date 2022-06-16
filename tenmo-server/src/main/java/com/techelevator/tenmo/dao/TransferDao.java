package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> transferHistory(Long id);

    boolean transfer (Transfer transfer, int accountFromId, int accountToId);


}
