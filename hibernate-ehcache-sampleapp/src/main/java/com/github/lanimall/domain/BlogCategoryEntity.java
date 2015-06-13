package com.github.lanimall.domain;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by FabienSanglier on 5/27/15.
 */
@Entity
@Table(name = "blogCategory", schema = "", catalog = "hibernatesample")
public class BlogCategoryEntity {
    private int blogCategoryid;
    private String name;
    private Collection<BlogPostCategoryEntity> posts;

    public Collection<BlogPostCategoryEntity> getPosts() {
        return posts;
    }

    public void setPosts(Collection<BlogPostCategoryEntity> posts) {
        this.posts = posts;
    }

    public BlogCategoryEntity() {
    }

    public BlogCategoryEntity(String name) {
        this.name = name;
    }

    @Id
    @Column(name = "blogCategoryid", nullable = false, insertable = true, updatable = true)
    public int getBlogCategoryid() {
        return blogCategoryid;
    }

    public void setBlogCategoryid(int blogCategoryid) {
        this.blogCategoryid = blogCategoryid;
    }

    @Basic
    @Column(name = "name", nullable = true, insertable = true, updatable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlogCategoryEntity that = (BlogCategoryEntity) o;

        if (blogCategoryid != that.blogCategoryid) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = blogCategoryid;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
