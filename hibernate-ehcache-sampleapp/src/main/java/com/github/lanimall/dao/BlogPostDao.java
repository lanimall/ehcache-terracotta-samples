package com.github.lanimall.dao;

import com.github.lanimall.domain.BlogPostEntity;
import java.util.List;

/**
 * Created by FabienSanglier on 5/27/15.
 */
public interface BlogPostDao {
    void saveBatch(BlogPostEntity[] obj);

    void save(BlogPostEntity obj);

    List<BlogPostEntity> findAll(int maxSize);

    void delete(BlogPostEntity obj);

    void deleteById(int id);

    void deleteAll();

    BlogPostEntity findById(int id);
}