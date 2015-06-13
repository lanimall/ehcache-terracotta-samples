package com.github.lanimall.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by FabienSanglier on 5/27/15.
 */
public class BlogPostCategoryEntityPK implements Serializable {
    private int blogPostid;
    private int blogCategoryid;

    @Column(name = "blogPostid", nullable = false, insertable = true, updatable = true)
    @Id
    public int getBlogPostid() {
        return blogPostid;
    }

    public void setBlogPostid(int blogPostid) {
        this.blogPostid = blogPostid;
    }

    @Column(name = "blogCategoryid", nullable = false, insertable = true, updatable = true)
    @Id
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

        BlogPostCategoryEntityPK that = (BlogPostCategoryEntityPK) o;

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
}
