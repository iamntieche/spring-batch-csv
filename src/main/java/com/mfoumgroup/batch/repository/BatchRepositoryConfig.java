package com.mfoumgroup.batch.repository;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@EnableJdbcRepositories
public class BatchRepositoryConfig extends AbstractJdbcConfiguration {
	@Autowired
	private DataSource dataSource;
	
	NamedParameterJdbcOperations operations() {
		return new NamedParameterJdbcTemplate(dataSource);
	}
}
