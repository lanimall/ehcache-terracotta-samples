package com.github.lanimall.services;

import com.github.lanimall.domain.BlogCategoryEntity;

import java.util.List;

/**
 * Created by FabienSanglier on 5/27/15.
 */

public interface BlogCategoryService {
    void saveBlogCategory(BlogCategoryEntity blogCategory);

    List<BlogCategoryEntity> findAllBlogCategories();

    void deleteBlogCategory(BlogCategoryEntity blogCategory);

    void deleteBlogCategory(int blogCategoryId);

    void deleteAll();

    BlogCategoryEntity getBlogCategory(int blogCategoryId);
}
