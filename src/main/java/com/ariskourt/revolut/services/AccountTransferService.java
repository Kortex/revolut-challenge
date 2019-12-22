package com.ariskourt.revolut.services;

import com.ariskourt.revolut.api.AccountTransferRequest;
import com.ariskourt.revolut.api.AccountTransferResponse;

public interface AccountTransferService {

    AccountTransferResponse transferAmount(AccountTransferRequest request);

}
