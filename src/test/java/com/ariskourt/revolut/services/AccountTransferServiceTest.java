package com.ariskourt.revolut.services;

import com.ariskourt.revolut.api.AccountTransferRequest;
import com.ariskourt.revolut.api.AccountTransferResponse;
import com.ariskourt.revolut.domain.BankAccount;
import com.ariskourt.revolut.utils.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountTransferServiceTest {

    private static final String FROM_ID = UUID.randomUUID().toString();
    private static final String TO_ID = UUID.randomUUID().toString();
    private static final Long AMOUNT = 1000L;

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
        from = createAccount(FROM_ID, 5000L);
        to = createAccount(TO_ID, 2000L);
        request = createRequest();
    }

    @Test
    public void transferAmount_WhenCalled_ResponseIsReturned() {
        when(queryService.getBankAccount(eq(FROM_ID))).thenReturn(from);
        when(queryService.getBankAccount(eq(TO_ID))).thenReturn(to);
        doNothing().when(validationService).validateTransferDetails(any(Pair.class), anyLong());
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

    private BankAccount createAccount(String id, Long balance) {
        var account = new BankAccount();
        account.setId(id);
        account.setAccountHolder("John Doe");
        account.setAccountBalance(balance);
        account.setCreatedAt(new Date());
        account.setVersion(1);
        return account;
    }

}