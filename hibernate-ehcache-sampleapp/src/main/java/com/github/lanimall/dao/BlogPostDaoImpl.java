package com.github.lanimall.dao;

import com.github.lanimall.domain.BlogCategoryEntity;
import com.github.lanimall.domain.BlogPostEntity;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by fabien.sanglier on 11/10/15.
 */
@Repository("blogPostDao")
public class BlogPostDaoImpl extends BaseDaoImpl implements BlogPostDao {
    @Override
    public void save(BlogPostEntity obj) {
        super.save(obj);
    }

    @Override
    public void saveBatch(BlogPostEntity[] obj) {
        super.saveBulk(obj);
    }

    @Override
    public List<BlogPostEntity> findAll(int maxSize) {
        return (List<BlogPostEntity>) super.getAll(BlogPostEntity.class, maxSize, true);

//        return (List<BlogCategoryEntity>) super.findAll("from " + BlogCategoryEntity.class.getSimpleName(), null, new QueryCacheProperty(true));
    }

    @Override
    public void delete(BlogPostEntity obj) {
        super.delete(obj);
    }

    @Override
    public void deleteById(int id) {
        Query query = getSession().createSQLQuery("delete from " + BlogPostEntity.class.getSimpleName() + " where blogpostid = :id");
        query.setInteger("id", id);
        query.executeUpdate();
    }

    @Override
    public void deleteAll() {
        super.deleteAll(BlogPostEntity.class);
    }

    @Override
    public BlogPostEntity findById(int id) {
        return (BlogPostEntity) super.findById(BlogPostEntity.class, id);
    }
}
