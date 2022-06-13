package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountDao dao;

    public AccountController(AccountDao dao) {
        this.dao = dao;
    }

    @GetMapping("account/{id}")
    public Account getAccount(@PathVariable int id) {
        return dao.getAccount(id);
    }

    @GetMapping("/accountId/{id}")
    public Long getAccountId(@PathVariable int id) {
        return dao.getUserId(id);
    }

    @GetMapping("/account/balance/{id}")
    public BigDecimal getBalance(@PathVariable int id) {
        return dao.getBalance(id);
    }
}
