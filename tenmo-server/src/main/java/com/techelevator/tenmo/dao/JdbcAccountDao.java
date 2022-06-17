package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccount(int id) {
        String sql = "select account_id from account where user_id = ?";
        int accountId = jdbcTemplate.queryForObject(sql, int.class, id);
        return null;
    }

    @Override
    public Long getUserId(int id) {
        return null;
    }

    @Override
    public Account findAccountById(int id) {
        Account account = null;
        String sql = "select * from account where user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public BigDecimal addToBalance(BigDecimal amountToAdd, int id) {
        Account account = findAccountById(id);
        BigDecimal newBalance = account.getBalance().add(amountToAdd);
        String sqlString = "update accounts set balance = ? where user_id = ?";
        try {
            jdbcTemplate.update(sqlString, newBalance, id);
        } catch (DataAccessException e) {
            System.out.println("Error accessing data");
        }
        return account.getBalance();
    }


    @Override
    public BigDecimal getBalance(int id) {
        String sql = "select balance from account where user_id = ?";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, id);
        return balance;
    }

    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();
        account.setAccountId(results.getLong("account_id"));
        account.setUserId(results.getLong("user_id"));
        account.setBalance(results.getBigDecimal("balance"));
        return account;
    }
}
