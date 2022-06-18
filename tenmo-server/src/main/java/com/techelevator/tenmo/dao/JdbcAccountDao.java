package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.math.BigDecimal;

@Service
public class JdbcAccountDao implements AccountDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao() {}

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account findUserById(int userId) {
        String sql = "select * from accounts where user_id = ?";
        Account account = null;
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
            account = mapRowToAccount(result);
        } catch (DataAccessException e) {
            System.out.println("Error");
        }
        return account;
    }

    @Override
    public Account findAccountById(int userId) {
        String sql = "select * from account where user_id = ?";
        Account account = null;
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
            account = mapRowToAccount(result);
        } catch (DataAccessException e) {
            System.out.println("Error");
        }
        return account;
    }

    @Override
    public BigDecimal getBalance(int userId) {
        String sql = "select balance from account where user_id = ?";
        SqlRowSet results = null;
        BigDecimal balance = null;
        try {
            results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                balance = results.getBigDecimal("balance");
            }
        } catch (DataAccessException e) {
            System.out.println("error");
        }
        return balance;
    }

    @Override
    public BigDecimal addToBalance(BigDecimal amountToAdd, int id) {
        Account account = findAccountById(id);
        BigDecimal newBalance = account.getBalance().add(amountToAdd);
        System.out.println("Your new balance is $" + newBalance);
        String sql = "update account set balance = ? where user_id = ?";
        try {
            jdbcTemplate.update(sql, newBalance, id);
        } catch (DataAccessException e) {
            System.out.println("error");
        }
        return account.getBalance();
    }

    @Override
    public BigDecimal subtractFromBalance(BigDecimal amountToSubtract, int id) {
        Account account= findAccountById(id);
        BigDecimal newBalance = account.getBalance().subtract(amountToSubtract);
        String sql = "update account set balance = ? where user_id = ?";
        try {
            jdbcTemplate.update(sql, newBalance, id);
        } catch (DataAccessException e) {
            System.out.println("error");
        }
        return account.getBalance();
    }


    private Account mapRowToAccount(SqlRowSet result) {
        Account account = new Account();
        account.setBalance(result.getBigDecimal("balance"));
        account.setAccountId(result.getInt("account_id"));
        account.setUserId(result.getInt("user_id"));
        return account;
    }
}
