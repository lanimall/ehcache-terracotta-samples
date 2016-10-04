package com.github.lanimall;

import com.github.lanimall.services.BlogMgtService;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by FabienSanglier on 5/27/15.
 */
public class AppMain {
    public static void main(final String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context.xml");
        applicationContext.registerShutdownHook();

        BlogMgtService blogPostService = (BlogMgtService) applicationContext.getBean("blogMgtService");
        blogPostService.addRandomBlogCategories(100);
        blogPostService.addRandomBlogs(100);

        //Get all blog categories from database
        long timingStart = System.nanoTime();
        for(int i=0; i< 10; i++) {
            System.out.println("-------- iteration "+ i);
            blogPostService.getAllCategoriesAndIterate(-1);
        }
        long timingCategoryFetching = System.nanoTime() - timingStart;

        //Get all blog posts from database
        timingStart = System.nanoTime();
        for(int i=0; i< 10; i++) {
            System.out.println("-------- iteration "+ i);
            blogPostService.getAllBlogsAndIterate(-1);
        }
        long timingBlogPostFetching = System.nanoTime() - timingStart;

        System.out.println("Waiting for a bit...");
        System.out.println(String.format("-------- Fetching all categories:%d ms", timingCategoryFetching / 1000000));
        System.out.println(String.format("-------- Fetching all blog posts:%d ms", timingBlogPostFetching / 1000000));

        Thread.sleep(10000);

        blogPostService.cleanAll();

        applicationContext.close();
    }
}
