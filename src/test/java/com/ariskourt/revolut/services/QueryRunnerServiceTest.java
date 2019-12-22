package com.ariskourt.revolut.services;

import io.agroal.api.AgroalDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class QueryRunnerServiceTest {

    @Mock private AgroalDataSource dataSource;

    private QueryRunnerService queryRunnerService;

    @BeforeEach
    void setUp() {
        queryRunnerService = new QueryRunnerService(dataSource);
    }

    @Test
    @DisplayName("When get method is called a queryRunner object is returned")
    public void get_WhenCalled_QueryRunnerIsReturned() {
        QueryRunner runner = queryRunnerService.get();
        assertNotNull(runner);
    }


}