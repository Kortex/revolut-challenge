package com.ariskourt.revolut.services;

import com.ariskourt.revolut.api.resources.AccountTransferRequest;
import com.ariskourt.revolut.api.resources.AccountTransferResponse;
import com.ariskourt.revolut.domain.BankAccount;
import com.ariskourt.revolut.exceptions.DataAccessException;
import com.ariskourt.revolut.utils.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import java.sql.SQLException;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class AccountTransferService {

    private static final String UPDATE_BALANCE_QUERY = "UPDATE bank_account "
        + "SET account_balance = ?, "
        + "updated_at = NOW(), "
        + "version = ((SELECT version FROM bank_account WHERE id = ?) + 1) "
        + "WHERE id = ?";

    private final QueryRunnerService databaseService;
    private final AccountValidationService validationService;

    @Transactional(rollbackOn = DataAccessException.class)
    public AccountTransferResponse transferAmount(AccountTransferRequest request) {
        log.info("Got incoming request with details {} validating", request);

        try {
            Pair<BankAccount, BankAccount> accountPair = validationService.validateAndGetAccounts(request);

            log.info("Transferring {} credits from account {} to account {}",
                request.getAmount(),
                accountPair.getLeft().getId(),
                accountPair.getRight().getId());

            var newFromBalance = accountPair.getLeft().getAccountBalance() - request.getAmount();
            var newToBalance  = accountPair.getRight().getAccountBalance() - request.getAmount();
            runUpdate(newFromBalance, accountPair.getLeft().getId());
            runUpdate(newToBalance, accountPair.getRight().getId());
            return new AccountTransferResponse(request.getAmount(), "Account funds transfer has been successful");
        } catch (SQLException e) {
            log.error("An error has occurred while performing a database operation", e);
            throw new DataAccessException(e);
        }

    }

    private void runUpdate(Long balance, String accountId) throws SQLException {
        databaseService
            .get()
            .update(UPDATE_BALANCE_QUERY, balance, accountId, accountId);
    }

}
