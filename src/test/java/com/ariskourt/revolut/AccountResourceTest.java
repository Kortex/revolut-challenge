package com.ariskourt.revolut;

import com.ariskourt.revolut.api.AccountTransferRequest;
import com.ariskourt.revolut.api.AccountTransferResponse;
import com.ariskourt.revolut.api.ErrorResponse;
import com.ariskourt.revolut.domain.BankAccount;
import com.ariskourt.revolut.exceptions.common.ApplicationError;
import com.ariskourt.revolut.services.QueryRunnerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@QuarkusTest
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class AccountResourceTest {

    private static final String FROM_ID = "42a238e1-a020-4ef2-a6dd-ab590f35ef12";
    private static final String TO_ID = "7c74d1d9-99f7-48b5-85b6-1397902dde17";

    private ObjectMapper mapper;

    @Inject
    QueryRunnerService queryRunnerService;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    public void transfer_WhenRequestDetailsAreOk_200isReturnedAlongWithResponse() throws JsonProcessingException {
        var request = createRequest(FROM_ID, TO_ID, 1000L);
        var response = given()
            .body(mapper.writeValueAsString(request))
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .when()
            .post("/accounts/transfer")
            .then()
            .log().all()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(AccountTransferResponse.class);
        assertNotNull(response);
        assertEquals(request.getFromAccount(), response.getFromAccount());
        assertEquals(request.getToAccount(), response.getToAccount());
        assertEquals(request.getAmount(), response.getAmount());
        assertThat(response.getMessage(), containsString("successful"));
    }

    @Test
    @Order(2)
    public void transfer_WhenRequestContainsNoMatchingAccounts_404isReturnedAlongWithErrorResponse() throws JsonProcessingException {
        var request = createRequest(UUID.randomUUID().toString(), UUID.randomUUID().toString(), 1000L);
        var response = given()
            .body(mapper.writeValueAsString(request))
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .when()
            .post("/accounts/transfer")
            .then()
            .log().all()
            .assertThat()
            .statusCode(404)
            .extract()
            .as(ErrorResponse.class);
        assertNotNull(response);
        assertNotNull(response.getMessage());
        assertNull(response.getStacktrace());
        assertEquals(ApplicationError.ACCOUNT_NOT_FOUND.getCode(), response.getErrorCode());
    }

    @Test
    @Order(3)
    public void transfer_WhenRequestContainsNoMatchingFromAccount_404isReturnedAlongWithErrorResponse() throws JsonProcessingException {
        var request = createRequest(UUID.randomUUID().toString(), TO_ID, 1000L);
        var response = given()
            .body(mapper.writeValueAsString(request))
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .when()
            .post("/accounts/transfer")
            .then()
            .log().all()
            .assertThat()
            .statusCode(404)
            .extract()
            .as(ErrorResponse.class);
        assertNotNull(response);
        assertNotNull(response.getMessage());
        assertNull(response.getStacktrace());
        assertEquals(ApplicationError.ACCOUNT_NOT_FOUND.getCode(), response.getErrorCode());
    }

    @Test
    @Order(4)
    public void transfer_WhenRequestContainsNoMatchingToAccount_404isReturnedAlongWithErrorResponse() throws JsonProcessingException {
        var request = createRequest(FROM_ID, UUID.randomUUID().toString(), 1000L);
        var response = given()
            .body(mapper.writeValueAsString(request))
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .when()
            .post("/accounts/transfer")
            .then()
            .log().all()
            .assertThat()
            .statusCode(404)
            .extract()
            .as(ErrorResponse.class);
        assertNotNull(response);
        assertNotNull(response.getMessage());
        assertNull(response.getStacktrace());
        assertEquals(ApplicationError.ACCOUNT_NOT_FOUND.getCode(), response.getErrorCode());
    }

    @Test
    @Order(5)
    public void transfer_WhenRequestContainsTheSameAccount_400isReturnedAlongWithErrorResponse() throws JsonProcessingException {
        var request = createRequest(FROM_ID, FROM_ID, 1000L);
        var response = given()
            .body(mapper.writeValueAsString(request))
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .when()
            .post("/accounts/transfer")
            .then()
            .log().all()
            .assertThat()
            .statusCode(400)
            .extract()
            .as(ErrorResponse.class);
        assertNotNull(response);
        assertNotNull(response.getMessage());
        assertNull(response.getStacktrace());
        assertEquals(ApplicationError.SAME_ACCOUNT_TRANSFER.getCode(), response.getErrorCode());
    }

    @Test
    @Order(6)
    public void transfer_WhenRequestAmountIsOverFromAccountBalance_400isReturnedAlongWithErrorResponse() throws JsonProcessingException {
        var request = createRequest(FROM_ID, TO_ID, 100000L);
        var response = given()
            .body(mapper.writeValueAsString(request))
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .when()
            .post("/accounts/transfer")
            .then()
            .log().all()
            .assertThat()
            .statusCode(400)
            .extract()
            .as(ErrorResponse.class);
        assertNotNull(response);
        assertNotNull(response.getMessage());
        assertNull(response.getStacktrace());
        assertEquals(ApplicationError.INSUFFICIENT_ACCOUNT_BALANCE.getCode(), response.getErrorCode());
    }

    @Test
    @Order(7)
    public void transfer_WhenFromAccountBalanceHasNoFunds_400isReturnedAlongWithErrorResponse() throws Exception {
        log.info("Zeroing out account balance for account {}", FROM_ID);
        queryRunnerService
            .getRunner()
            .update("UPDATE bank_account SET account_balance = ? WHERE id = ?", 0, FROM_ID);

        var request = createRequest(FROM_ID, TO_ID, 1000L);
        var response = given()
            .body(mapper.writeValueAsString(request))
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .when()
            .post("/accounts/transfer")
            .then()
            .log().all()
            .assertThat()
            .statusCode(400)
            .extract()
            .as(ErrorResponse.class);
        assertNotNull(response);
        assertNotNull(response.getMessage());
        assertNull(response.getStacktrace());
        assertEquals(ApplicationError.INSUFFICIENT_ACCOUNT_BALANCE.getCode(), response.getErrorCode());

        log.info("Re-instating account balance for account {}", FROM_ID);
        queryRunnerService
            .getRunner()
            .update("UPDATE bank_account SET account_balance = ? WHERE id = ?", 10000, FROM_ID);
    }

    @Test
    @Order(8)
    public void list_whenListingAccountsAndDataIsPresent_200AndCorrectResponse() {
        List<BankAccount> accounts = new ArrayList<>();
        accounts = given()
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .get("/accounts")
            .then()
            .log().all()
            .assertThat()
            .statusCode(200)
            .extract()
            .body()
            .as(accounts.getClass());
        assertFalse(accounts.isEmpty());
        assertEquals(2, accounts.size());
    }

    private AccountTransferRequest createRequest(String fromId, String toId, Long amount) {
        var request = new AccountTransferRequest();
        request.setFromAccount(fromId);
        request.setToAccount(toId);
        request.setAmount(amount);
        return request;
    }

}