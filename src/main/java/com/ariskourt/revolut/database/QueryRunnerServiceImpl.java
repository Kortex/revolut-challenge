package com.ariskourt.revolut.database;

import io.agroal.api.AgroalDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;

import javax.enterprise.context.ApplicationScoped;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class QueryRunnerServiceImpl implements QueryRunnerService {

    private final AgroalDataSource dataSource;

    /***
     * Method that creates and return a new {@link QueryRunner} object used to issue SQL statements
     * for the given datasource
     *
     * @return - A configured QueryRunner object for the given datasource
     */
    @Override
    public QueryRunner getRunner() {
	return new QueryRunner(dataSource);
    }

}
