package com.github.lanimall.dao;

import com.github.lanimall.domain.BlogCategoryEntity;
import com.github.lanimall.domain.QueryCacheProperty;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by FabienSanglier on 5/27/15.
 */

@Repository("blogCategoryDao")
public class BlogCategoryDaoImpl extends BaseDaoImpl implements BlogCategoryDao {
    public static final Logger log = LoggerFactory.getLogger(BlogCategoryDaoImpl.class);

    @Override
    public void save(BlogCategoryEntity obj) {
        super.save(obj);
    }

    @Override
    public void saveBatch(BlogCategoryEntity[] obj) {
        super.saveBulk(obj);
    }

    @Override
    public List<BlogCategoryEntity> findAll(int maxSize) {
        return (List<BlogCategoryEntity>) super.getAll(BlogCategoryEntity.class, maxSize, true);

//        return (List<BlogCategoryEntity>) super.findAll("from " + BlogCategoryEntity.class.getSimpleName(), null, new QueryCacheProperty(true));
    }

    @Override
    public void delete(BlogCategoryEntity obj) {
        super.delete(obj);
    }

    @Override
    public void deleteAll() {
        super.deleteAll(BlogCategoryEntity.class);
    }

    @Override
    public void deleteById(int id) {
        Query query = getSession().createSQLQuery("delete from " + BlogCategoryEntity.class.getSimpleName() + " where blogCategoryid = :id");
        query.setInteger("id", id);
        query.executeUpdate();
    }

    @Override
    public BlogCategoryEntity findById(int id) {
        return (BlogCategoryEntity) super.findById(BlogCategoryEntity.class, id);
    }
}
