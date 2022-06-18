package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    Account getAccount(int id);

    int getAccountId(int id);

    int getUserId(int id);

    BigDecimal getBalance(int id);

    Account findAccountById(int id);

    BigDecimal addToBalance(BigDecimal amountToAdd, int id);
}
