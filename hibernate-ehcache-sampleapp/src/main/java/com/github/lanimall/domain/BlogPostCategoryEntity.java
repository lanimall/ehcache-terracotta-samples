package com.github.lanimall.domain;

import javax.persistence.*;

/**
 * Created by FabienSanglier on 5/27/15.
 */
@Entity
@Table(name = "blogPostCategory", schema = "", catalog = "hibernatesample")
@IdClass(BlogPostCategoryEntityPK.class)
public class BlogPostCategoryEntity {
    private int blogPostid;
    private int blogCategoryid;
    private BlogCategoryEntity blogCategoryByBlogCategoryid;
    private BlogPostEntity blogPostByBlogPostid;

    @Id
    @Column(name = "blogPostid", nullable = false, insertable = true, updatable = true)
    public int getBlogPostid() {
        return blogPostid;
    }

    public void setBlogPostid(int blogPostid) {
        this.blogPostid = blogPostid;
    }

    @Id
    @Column(name = "blogCategoryid", nullable = false, insertable = true, updatable = true)
    public int getBlogCategoryid() {
        return blogCategoryid;
    }

    public void setBlogCategoryid(int blogCategoryid) {
        this.blogCategoryid = blogCategoryid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlogPostCategoryEntity that = (BlogPostCategoryEntity) o;

        if (blogCategoryid != that.blogCategoryid) return false;
        if (blogPostid != that.blogPostid) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = blogPostid;
        result = 31 * result + blogCategoryid;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "blogCategoryid", referencedColumnName = "blogCategoryid", nullable = false)
    public BlogCategoryEntity getBlogCategoryByBlogCategoryid() {
        return blogCategoryByBlogCategoryid;
    }

    public void setBlogCategoryByBlogCategoryid(BlogCategoryEntity blogCategoryByBlogCategoryid) {
        this.blogCategoryByBlogCategoryid = blogCategoryByBlogCategoryid;
    }

    @ManyToOne
    @JoinColumn(name = "blogPostid", referencedColumnName = "blogpostid", nullable = false)
    public BlogPostEntity getBlogPostByBlogPostid() {
        return blogPostByBlogPostid;
    }

    public void setBlogPostByBlogPostid(BlogPostEntity blogPostByBlogPostid) {
        this.blogPostByBlogPostid = blogPostByBlogPostid;
    }
}
