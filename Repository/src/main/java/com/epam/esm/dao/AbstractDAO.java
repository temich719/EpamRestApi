package com.epam.esm.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public abstract class AbstractDAO {

    protected final SessionFactory sessionFactory;

    @Autowired
    public AbstractDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
