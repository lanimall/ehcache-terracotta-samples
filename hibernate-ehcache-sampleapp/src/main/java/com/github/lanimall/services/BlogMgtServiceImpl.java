package com.github.lanimall.services;

import com.github.lanimall.dao.BlogCategoryDao;
import com.github.lanimall.dao.BlogPostDao;
import com.github.lanimall.domain.BlogCategoryEntity;
import com.github.lanimall.domain.BlogPostEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by fabien.sanglier on 11/10/15.
 */
@Service("blogMgtService")
@Transactional
public class BlogMgtServiceImpl implements BlogMgtService {
    @Autowired
    private BlogCategoryDao blogCategoryDao;

    @Autowired
    private BlogPostDao blogPostDao;

    @Override
    public void addRandomBlogCategories(int count) {
        BlogCategoryEntity[] categoryEntities = new BlogCategoryEntity[count];
        for(int i=0; i< count; i++) {
            categoryEntities[i]=new BlogCategoryEntity("cat" + i);
        }
        blogCategoryDao.saveBatch(categoryEntities);
    }

    @Override
    public void addRandomBlogs(int count) {
        List<BlogCategoryEntity> blogCategories = blogCategoryDao.findAll(-1);
        int blogCategoriesSize = blogCategories.size();
        System.out.println("-------- blogCategories: "+ blogCategoriesSize);

        Random rdm = new Random(System.currentTimeMillis());

        BlogPostEntity[] blogPostToAdd = new BlogPostEntity[count];
        for(int i=0; i < count; i++) {
            System.out.println("-------- iteration "+ i);
            BlogPostEntity blog = new BlogPostEntity();
            blog.setTitle("some title " + i);
            blog.setSummary("some summary " + i);
            blog.setBody("some post content " + i);

            Timestamp now = new Timestamp(new Date().getTime());
            blog.setCreatedDateTime(now);
            blog.setModifiedDateTime(now);
            blog.setDateposted(now);

            int catCountToPick = rdm.nextInt(blogCategoriesSize);
            System.out.println("-------- blogCategories to pick: "+ catCountToPick);
            if(null != blogCategories && catCountToPick > 0) {
                ArrayList<BlogCategoryEntity> categoriesToAdd = new ArrayList<BlogCategoryEntity>(catCountToPick);
                int addedCounter=0;
                while(addedCounter < catCountToPick){
                    int indexToPick = rdm.nextInt(blogCategories.size());
                    if(!categoriesToAdd.contains(blogCategories.get(indexToPick))){
                        categoriesToAdd.add(blogCategories.get(indexToPick));
                        addedCounter++;
                    }
                }
                blog.setBlogCategories(categoriesToAdd);
            }
            blogPostToAdd[i] = blog;
        }
        blogPostDao.saveBatch(blogPostToAdd);
    }

    @Override
    public List<BlogPostEntity> getAllBlogsAndIterate(int limit) {
        List<BlogPostEntity> blogPostEntities = blogPostDao.findAll(limit);

        //i know it's redundant here...but this is just to exercise the cache and see if it works fine...
        List<BlogPostEntity> iteratedList = new ArrayList<BlogPostEntity>();
        for (BlogPostEntity blogPost : blogPostEntities) {
            BlogPostEntity entity = blogPostDao.findById(blogPost.getBlogpostid());
            iteratedList.add(entity);
            //System.out.println(blogCategoryEntity.toString());
        }
        return iteratedList;
    }

    @Override
    public List<BlogCategoryEntity> getAllCategoriesAndIterate(int limit) {
        List<BlogCategoryEntity> blogCategories = blogCategoryDao.findAll(limit);

        //i know it's redundant here...but this is just to exercise the cache and see if it works fine...
        List<BlogCategoryEntity> iteratedList = new ArrayList<BlogCategoryEntity>();
        for (BlogCategoryEntity blogCategoryEntity : blogCategories) {
            BlogCategoryEntity entity = blogCategoryDao.findById(blogCategoryEntity.getBlogCategoryid());
            //System.out.println(blogCategoryEntity.toString());
            iteratedList.add(entity);
        }
        return iteratedList;
    }

    @Override
    public void cleanAll() {
        blogPostDao.deleteAll();
        blogCategoryDao.deleteAll();
    }
}
