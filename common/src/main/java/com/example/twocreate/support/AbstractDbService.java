package com.example.twocreate.support;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDbService extends LoggerSupport {

    @Autowired
    protected DbTemplate db;
}
