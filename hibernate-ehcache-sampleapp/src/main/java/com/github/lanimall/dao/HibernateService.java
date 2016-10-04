package com.github.lanimall.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * Created by fabien.sanglier on 11/10/15.
 */
@Service("HibernateService")
public class HibernateService {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final int JDBC_BATCHSIZE_DEFAULT = 50;

    @Autowired
    private SessionFactory sessionFactory;

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void clearSession() {
        clearSession(false);
    }

    public void clearSession(boolean close) {
        getSession().flush();
        getSession().clear();
        if (close)
            getSession().disconnect();
    }
}
