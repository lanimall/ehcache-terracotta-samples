package com.github.lanimall;

import com.github.lanimall.domain.BlogCategoryEntity;
import com.github.lanimall.services.BlogCategoryService;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by FabienSanglier on 5/27/15.
 */
public class AppMain {
    public static void main(final String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context.xml");
        applicationContext.registerShutdownHook();

        BlogCategoryService service = (BlogCategoryService) applicationContext.getBean("blogCategoryService");

        /*
         * Create blogcategories
         */
        service.saveBlogCategory(new BlogCategoryEntity("html"));
        service.saveBlogCategory(new BlogCategoryEntity("js"));
        service.saveBlogCategory(new BlogCategoryEntity("java"));
        service.saveBlogCategory(new BlogCategoryEntity("sql"));

        /*
         * Get all blog categories from database
         */
        for(int i=0; i< 100; i++) {
            List<BlogCategoryEntity> blogCategories = service.findAllBlogCategories();
            for (BlogCategoryEntity blogCategoryEntity : blogCategories) {
                System.out.println(blogCategoryEntity.toString());
            }
        }

        System.out.println("Waiting for a bit...");
        Thread.sleep(1000);

        /**
         * delete all blog categories
         */
        service.deleteAll();

        /*
         * Get all blog categories from database
         */
        for(int i=0; i< 100; i++) {
            List<BlogCategoryEntity> blogCategories = service.findAllBlogCategories();
            for (BlogCategoryEntity blogCategoryEntity : blogCategories) {
                System.out.println(blogCategoryEntity.toString());
            }
        }

        applicationContext.close();
    }
}
