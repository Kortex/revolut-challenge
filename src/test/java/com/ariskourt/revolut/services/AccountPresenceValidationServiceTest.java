package com.ariskourt.revolut.services;

import com.ariskourt.revolut.api.resources.AccountTransferRequest;
import com.ariskourt.revolut.domain.BankAccount;
import com.ariskourt.revolut.exceptions.BankAccountNotFoundException;
import com.ariskourt.revolut.repositories.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountPresenceValidationServiceTest {

    private static final String FROM_UUID = "7196c512-6fa7-4d97-a27a-cdced71d4ca3";
    private static final String TO_UUID = "1e1229d3-0d76-4f52-9fcb-903d577211e8";

    @Mock
    private BankAccountRepository repository;

    private AccountPresenceValidationService service;

    private AccountTransferRequest request;

    @BeforeEach
    void setUp() {
        service = new AccountPresenceValidationService(repository);
        request = createRequest();
    }

    @Test
    public void validateRequest_WhenBothAccountsPresent_validationPasses() {
        when(repository.findById(anyString())).thenReturn(new BankAccount());
        try {
            service.validateRequest(request);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void validateRequest_WhenBothAccountsNotPresent_validationFails() {
        when(repository.findById(FROM_UUID)).thenReturn(null);
        when(repository.findById(TO_UUID)).thenReturn(null);
        assertThrows(BankAccountNotFoundException.class, () -> service.validateRequest(request));
    }

    @Test
    public void validateRequest_WhenFromAccountNotPresent_validationFails() {
        when(repository.findById(FROM_UUID)).thenReturn(null);
        when(repository.findById(TO_UUID)).thenReturn(new BankAccount());
        assertThrows(BankAccountNotFoundException.class, () -> service.validateRequest(request));
    }

    @Test
    public void validateRequest_WhenToAccountNotPresent_validationFails() {
        when(repository.findById(FROM_UUID)).thenReturn(new BankAccount());
        when(repository.findById(TO_UUID)).thenReturn(null);
        assertThrows(BankAccountNotFoundException.class, () -> service.validateRequest(request));
    }

    private AccountTransferRequest createRequest() {
        AccountTransferRequest request = new AccountTransferRequest();
        request.setFromAccount(FROM_UUID);
        request.setToAccount(TO_UUID);
        request.setAmount(1000L);
        return request;
    }

}