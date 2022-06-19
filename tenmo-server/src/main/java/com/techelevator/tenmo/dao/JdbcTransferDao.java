package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean transfer(Transfer transfer, int accountFromId, int accountToId) throws Exception {
        String sql = "insert into transfer (transfer_type_id, transfer_status_id, account_to, account_from, amount)" +
                "values (?, ?, ?, ?, ?);" +
                "update account set balance = balance - ? where account_id = ?;" +
                "update account set balance = balance + ? where account_id = ?;";
            return jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(),
                    transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount(), transfer.getAmount(),
                    accountFromId, transfer.getAmount(), accountToId) == 3;
    }

    @Override
    public Transfer transferDetails(int transferId) {
        Transfer transferDetails = null;
        String sql = "select * from transfer where transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()){
            transferDetails = mapRowToTransfer(results);
        }
        return transferDetails;
    }

    @Override
    public List<Transfer> transferHistory(Long account_id) {
        List<Transfer> transferHistory = new ArrayList<>();
        String sql = "select * from transfer where account_from = ? or account_to = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account_id, account_id);
        while (results.next()){
            Transfer history = mapRowToTransfer(results);
            transferHistory.add(history);
        }
        return transferHistory;
    }



    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferID(results.getLong("transfer_id"));
        transfer.setTransferTypeId(results.getLong("transfer_type_id"));
        transfer.setTransferStatusId(results.getLong("transfer_status_id"));
        transfer.setAccountFrom(results.getLong("account_from"));
        transfer.setAccountTo(results.getLong("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }
}
