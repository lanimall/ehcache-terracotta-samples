package com.github.lanimall.dao;

import com.github.lanimall.domain.BlogCategoryEntity;

import java.util.List;

/**
 * Created by FabienSanglier on 5/27/15.
 */
public interface BlogCategoryDao {
    void saveBatch(BlogCategoryEntity[] obj);

    void save(BlogCategoryEntity obj);

    List<BlogCategoryEntity> findAll(int maxSize);

    void delete(BlogCategoryEntity obj);

    void deleteById(int id);

    void deleteAll();

    BlogCategoryEntity findById(int id);
}