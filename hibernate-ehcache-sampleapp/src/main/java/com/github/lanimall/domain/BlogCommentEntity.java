package com.github.lanimall.domain;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by FabienSanglier on 5/27/15.
 */
@Entity
@Table(name = "blogComment", schema = "", catalog = "hibernatesample")
public class BlogCommentEntity {
    private int blogCommentid;
    private String author;
    private String comment;
    private Timestamp createdDateTime;
    private Byte deleted;

    @Id
    @Column(name = "blogCommentid", nullable = false, insertable = true, updatable = true)
    public int getBlogCommentid() {
        return blogCommentid;
    }

    public void setBlogCommentid(int blogCommentid) {
        this.blogCommentid = blogCommentid;
    }

    @Basic
    @Column(name = "author", nullable = true, insertable = true, updatable = true, length = 255)
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Basic
    @Column(name = "comment", nullable = true, insertable = true, updatable = true, length = 2147483647)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "createdDateTime", nullable = true, insertable = true, updatable = true)
    public Timestamp getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Timestamp createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @Basic
    @Column(name = "deleted", nullable = true, insertable = true, updatable = true)
    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlogCommentEntity that = (BlogCommentEntity) o;

        if (blogCommentid != that.blogCommentid) return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (createdDateTime != null ? !createdDateTime.equals(that.createdDateTime) : that.createdDateTime != null)
            return false;
        if (deleted != null ? !deleted.equals(that.deleted) : that.deleted != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = blogCommentid;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (createdDateTime != null ? createdDateTime.hashCode() : 0);
        result = 31 * result + (deleted != null ? deleted.hashCode() : 0);
        return result;
    }
}
