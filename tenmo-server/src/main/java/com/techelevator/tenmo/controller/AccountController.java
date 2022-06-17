package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private UserDao userDao;

    private AccountDao dao;

    public AccountController(AccountDao dao, JdbcUserDao userDao) {
        this.dao = dao;
        this.userDao = userDao;
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

    @GetMapping("/user")
    public List<User> getAllUsers(){
        List<User> users = userDao.findAll();
        return users;
    }
}
