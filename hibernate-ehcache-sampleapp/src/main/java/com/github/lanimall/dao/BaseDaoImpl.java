package com.github.lanimall.dao;

import com.github.lanimall.domain.QueryCacheProperty;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
@Repository("BaseDao")
public abstract class BaseDaoImpl {
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

    public Serializable save(Object domain) {
        //return getHibernateTemplate().save(domain);
        saveOrUpdate(domain);
        return null;
    }

    public void saveBulk(final Object[] domain) {
        saveBulk(domain, JDBC_BATCHSIZE_DEFAULT);
    }

    public void saveBulk(final Object[] domain, final int batchSize) {
        for (int i = 0; i < domain.length; i++) {
            getSession().save(domain[i]);
            if (i % batchSize == 0) { //batchSize should be same as the JDBC batch size
                //flush a batch of inserts and release memory:
                clearSession();
            }
        }

        clearSession();
    }

    public void delete(Object domain) {
        getSession().delete(domain);
    }

    public void deleteAll(final Class typeOfObject) {
        Query query = getSession().createQuery("delete from " + typeOfObject.getName());
        query.executeUpdate();
    }

    public List getAll(final Class typeOfObject, boolean useCache) {
        return getAll(typeOfObject, null, true, 0, -1, new QueryCacheProperty(useCache));
    }

    public List getAll(final Class typeOfObject, final int maxResultSize, boolean useCache) {
        return getAll(typeOfObject, null, true, 0, maxResultSize, new QueryCacheProperty(useCache));
    }

    public List getAll(final Class typeOfObject, final String sortColumn, final boolean sortAsc, final int firstResult, final int maxResultSize, final QueryCacheProperty queryCacheProperty) {
        Criteria criteria = getSession().createCriteria(typeOfObject);
        if (null != sortColumn) {
            if (sortAsc)
                criteria.addOrder(Order.asc(sortColumn));
            else
                criteria.addOrder(Order.desc(sortColumn));
        }

        if (firstResult > -1)
            criteria.setFirstResult(firstResult);

        if (maxResultSize > 0) {
            criteria.setMaxResults(maxResultSize);
            criteria.setFetchSize(maxResultSize);
        }

        if (queryCacheProperty != null) {
            criteria.setCacheable(queryCacheProperty.isCachable());
            if(null != queryCacheProperty.getCacheMode())
                criteria.setCacheMode(queryCacheProperty.getCacheMode());

            if(null != queryCacheProperty.getCacheRegion())
                criteria.setCacheRegion(queryCacheProperty.getCacheRegion());
        }

        return criteria.list();
    }

    protected void update(Object domain) {
        getSession().update(domain);
    }

    protected void saveOrUpdate(Object domain) {
        getSession().saveOrUpdate(domain);
        getSession().flush();
    }

    public int updateusingHQL(final String queryString, final Object... params) {
        return updateusingHQL(queryString, null, params);
    }

    public int updateusingHQL(final String queryString, final Object[] positionParams, final QueryCacheProperty queryCacheProperty) {
        Query query = getSession().createQuery(queryString);
        if (queryCacheProperty != null) {
            query.setCacheable(queryCacheProperty.isCachable());
            query.setCacheMode(queryCacheProperty.getCacheMode());
            query.setCacheRegion(queryCacheProperty.getCacheRegion());
        }
        for (int i = 0; i < positionParams.length; i++) {
            query.setParameter(i, positionParams[i]);
        }
        return query.executeUpdate();
    }

    protected Object findById(Class klass, Serializable id) {
        return getSession().get(klass, id);
    }

    protected List find(String queryString, final Map<String, Object> params, boolean useCache) {
        return findbyNamedQueryAndNamedParams(queryString, params, new QueryCacheProperty(useCache));
    }

    protected Object findUniqueByNamedParams(final String queryString, final Map<String, Object> nameParams, boolean useCache) {
        return findUniqueByNamedParams(queryString, nameParams, new QueryCacheProperty(useCache));
    }

    protected Object findUniqueByNamedParams(final String queryString, final Map<String, Object> nameParams, final QueryCacheProperty queryCacheProperty) {
        Query query = getSession().createQuery(queryString);

        String[] paramNames = nameParams.keySet().toArray(new String[nameParams.size()]);
        Object[] values = nameParams.values().toArray();
        for (int i = 0; i < paramNames.length; i++) {
            query.setParameter(paramNames[i], values[i]);
        }

        return findUnique(query, queryCacheProperty);
    }

    protected Object findUniqueByPositionPArams(final String queryString, final Object[] positionParams, final QueryCacheProperty queryCacheProperty) {
        Query query = getSession().createQuery(queryString);
        for (int i = 0; i < positionParams.length; i++) {
            Object object = positionParams[i];
            if (object instanceof Map) {
                Map<String, Object> map = (Map) object;
                for (String key : map.keySet()) {
                    Object val = map.get(key);
                    if (val instanceof Collection) {
                        query.setParameterList(key, (Collection) val);
                    } else {
                        query.setParameter(key, val);
                    }
                }
            } else
                query.setParameter(i, positionParams[i]);
        }

        if (queryCacheProperty != null) {
            query.setCacheable(queryCacheProperty.isCachable());
            query.setCacheMode(queryCacheProperty.getCacheMode());
            query.setCacheRegion(queryCacheProperty.getCacheRegion());
        }

        return findUnique(query, null);
    }

    protected Object findUnique(final Query query, final QueryCacheProperty queryCacheProperty) {
        if (query == null)
            throw new IllegalArgumentException();

        if (queryCacheProperty != null) {
            query.setCacheable(queryCacheProperty.isCachable());
            query.setCacheMode(queryCacheProperty.getCacheMode());
            query.setCacheRegion(queryCacheProperty.getCacheRegion());
        }

        return query.uniqueResult();
    }

    protected List findAll(final String queryString, final Object[] positionParams, final QueryCacheProperty queryCacheProperty) {
        Query query = getSession().createQuery(queryString);
        if(null != positionParams) {
            for (int i = 0; i < positionParams.length; i++) {
                Object object = positionParams[i];
                if (object instanceof Map) {
                    Map<String, Object> map = (Map) object;
                    for (String key : map.keySet()) {
                        Object val = map.get(key);
                        if (val instanceof Collection) {
                            query.setParameterList(key, (Collection) val);
                        } else {
                            query.setParameter(key, val);
                        }
                    }
                } else
                    query.setParameter(i, positionParams[i]);
            }
        }

        if (queryCacheProperty != null) {
            query.setCacheable(queryCacheProperty.isCachable());
            query.setCacheMode(queryCacheProperty.getCacheMode());
            query.setCacheRegion(queryCacheProperty.getCacheRegion());
        }

        return query.list();
    }

    protected <T> List<T> findbyNamedQueryAndNamedParams(String queryName, Map<String, Object> namedParams, final QueryCacheProperty queryCacheProperty) {
        String[] paramNames = namedParams.keySet().toArray(new String[namedParams.size()]);
        Object[] values = namedParams.values().toArray();
        Query query = getSession().getNamedQuery(queryName);
        for (int i = 0; i < paramNames.length; i++) {
            query.setParameter(paramNames[i], values[i]);
        }

        if (queryCacheProperty != null) {
            query.setCacheable(queryCacheProperty.isCachable());
            query.setCacheMode(queryCacheProperty.getCacheMode());
            query.setCacheRegion(queryCacheProperty.getCacheRegion());
        }

        return (List<T>) query.list();
    }

    protected static class Wrapper<T> {
        protected List<T> results;
        protected int count;
    }
}