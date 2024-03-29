package com.ariskourt.revolut.services;

import com.ariskourt.revolut.api.AccountTransferRequest;
import com.ariskourt.revolut.api.AccountTransferResponse;
import com.ariskourt.revolut.database.AccountQueryService;
import com.ariskourt.revolut.database.AccountUpdateService;
import com.ariskourt.revolut.domain.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountTransferServiceTest {

    private static final String FROM_ID = UUID.randomUUID().toString();
    private static final String TO_ID = UUID.randomUUID().toString();
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(1000.0);

    @Mock private AccountQueryService queryService;
    @Mock private TransferValidationService validationService;
    @Mock private AccountUpdateService updateService;

    private AccountTransferService transferService;
    private BankAccount from;
    private BankAccount to;
    private AccountTransferRequest request;

    @BeforeEach
    void setUp() {
        transferService = new AccountTransferServiceImpl(queryService, validationService, updateService);
        from = createAccount(FROM_ID, "5000.00");
        to = createAccount(TO_ID, "2000.0");
        request = createRequest();
    }

    @Test
    public void transferAmount_WhenCalled_ResponseIsReturned() {
        when(queryService.getAccountBy(eq(FROM_ID))).thenReturn(from);
        when(queryService.getAccountBy(eq(TO_ID))).thenReturn(to);
        doNothing().when(validationService).validateTransferDetails(any(BankAccount.class), any(BankAccount.class), any(BigDecimal.class));
        doNothing().when(updateService).updateAccount(any(BankAccount.class));
        AccountTransferResponse response = transferService.transferAmount(request);
        assertNotNull(response);
        assertEquals(FROM_ID, response.getFromAccount());
        assertEquals(from.getAccountBalance(), response.getFromBalance());
        assertEquals(TO_ID, response.getToAccount());
        assertEquals(to.getAccountBalance(), response.getToBalance());
        assertEquals(AMOUNT, response.getAmount());
        assertNotNull(response.getMessage());
    }

    private AccountTransferRequest createRequest() {
        var request = new AccountTransferRequest();
        request.setFromAccount(FROM_ID);
        request.setToAccount(TO_ID);
        request.setAmount(AMOUNT);
        return request;
    }

    private BankAccount createAccount(String id, String amount) {
        var account = new BankAccount();
        account.setId(id);
        account.setAccountHolder("John Doe");
        account.setAccountBalance(new BigDecimal(amount));
        account.setCreatedAt(new Date());
        account.setVersion(1);
        return account;
    }

}