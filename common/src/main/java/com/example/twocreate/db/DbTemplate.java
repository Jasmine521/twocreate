package com.example.twocreate.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DbTemplate {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    final JdbcTemplate jdbcTemplate;

    private Map<Class<?>, Mapper<?>>
}
