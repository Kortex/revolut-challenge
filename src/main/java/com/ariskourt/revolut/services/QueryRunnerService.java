package com.ariskourt.revolut.services;

import io.agroal.api.AgroalDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;

import javax.enterprise.context.ApplicationScoped;

import java.util.function.Supplier;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class QueryRunnerService implements Supplier<QueryRunner> {

    private final AgroalDataSource dataSource;

    /***
     * Method implemented from the {@link Supplier} interface. Returns a {@link QueryRunner} using the injected
     * datasource.
     *
     * @return - A configured QueryRunner object for the given datasource
     */
    @Override
    public QueryRunner get() {
	return new QueryRunner(dataSource);
    }

}