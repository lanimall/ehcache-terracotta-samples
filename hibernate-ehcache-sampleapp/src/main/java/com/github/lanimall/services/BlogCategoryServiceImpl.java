package com.github.lanimall.services;

import com.github.lanimall.dao.BlogCategoryDao;
import com.github.lanimall.domain.BlogCategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by FabienSanglier on 5/27/15.
 */
@Service("blogCategoryService")
@Transactional
public class BlogCategoryServiceImpl implements BlogCategoryService {

    @Autowired
    private BlogCategoryDao dao;

    @Override
    public void saveBlogCategory(BlogCategoryEntity blogCategory) {
        dao.save(blogCategory);
    }

    @Override
    public List<BlogCategoryEntity> findAllBlogCategories() {

        return dao.findAll();
    }

    @Override
    public void deleteBlogCategory(BlogCategoryEntity blogCategory) {
        dao.delete(blogCategory);
    }

    @Override
    public void deleteBlogCategory(int blogCategoryId) {
        dao.deleteById(blogCategoryId);
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }

    @Override
    public BlogCategoryEntity getBlogCategory(int blogCategoryId) {

        return dao.findById(blogCategoryId);
    }
}
