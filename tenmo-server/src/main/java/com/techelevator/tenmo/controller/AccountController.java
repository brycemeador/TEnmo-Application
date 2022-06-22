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

    //Path to get the user's Account
    @GetMapping("/account/{id}")
    public Account getAccount(@PathVariable int id) {
        return dao.getAccount(id);
    }

    //Path to get the user's account ID
    @GetMapping("/user/{id}")
    public int getAccountId(@PathVariable int id) {
        return dao.getAccountId(id);
    }

    //Path to get the user's balance
    @GetMapping("/account/balance/{id}")
    public BigDecimal getBalance(@PathVariable int id) {
        return dao.getBalance(id);
    }

    //Path to get all users
    @GetMapping("/user")
    public List<User> getAllUsers(){
        List<User> users = userDao.findAll();
        return users;
    }

    //Path to get the user's user ID
    @GetMapping("/accountId/{id}")
    public int getUserId(@PathVariable int id){
        return dao.getUserId(id);
    }

    //Path to get the user's username
    @GetMapping("/username/{id}")
    public String getUsername(@PathVariable int id){
        return userDao.getUsername(id);
    }
}
