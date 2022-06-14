package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.TransferDao;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferController {

    private JdbcTransferDao dao;

    public TransferController(JdbcTransferDao dao) {
        this.dao = dao;
    }


}
