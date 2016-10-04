package com.github.lanimall.services;

import com.github.lanimall.domain.BlogCategoryEntity;
import com.github.lanimall.domain.BlogPostEntity;

import java.util.List;

/**
 * Created by FabienSanglier on 5/27/15.
 */

public interface BlogMgtService {
    void addRandomBlogCategories(int count);

    void addRandomBlogs(int count);

    List<BlogPostEntity> getAllBlogsAndIterate(int limit);

    List<BlogCategoryEntity> getAllCategoriesAndIterate(int limit);

    void cleanAll();
}