package com.ariskourt.revolut.services;

import com.ariskourt.revolut.api.resources.AccountTransferRequest;
import com.ariskourt.revolut.exceptions.BankAccountNotFoundException;
import com.ariskourt.revolut.repositories.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class AccountPresenceValidationService {

    private final BankAccountRepository repository;

    @Transactional
    public void validateRequest(AccountTransferRequest request) {

       var fromAccount = repository.findById(request.getFromAccount());
       var toAccount = repository.findById(request.getToAccount());

       if (fromAccount == null && toAccount == null) {
           log.error("No bank accounts with id {} and {} have been found", request.getFromAccount(), request.getToAccount());
           throw new BankAccountNotFoundException("No fromAccount and toAccount found with id {1} and {2} have been found",
               request.getFromAccount(), request.getToAccount());
       }

       if (fromAccount == null) {
           log.error("No fromAccount with id {} has been found", request.getFromAccount());
           throw new BankAccountNotFoundException("No fromAccount with the given id {1} has been found", request.getFromAccount());
       }

       if (toAccount == null) {
           log.error("No toAccount with id {} has been found", request.getToAccount());
           throw new BankAccountNotFoundException("No toAccount with id {1} has been found", request.getToAccount());
       }

    }

}
