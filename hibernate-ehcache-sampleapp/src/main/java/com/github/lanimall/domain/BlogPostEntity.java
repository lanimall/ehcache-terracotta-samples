package com.github.lanimall.domain;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by FabienSanglier on 5/27/15.
 */
@Entity
@Table(name = "blogPost", schema = "", catalog = "hibernatesample")
public class BlogPostEntity {
    private int blogpostid;
    private String title;
    private String summary;
    private String body;
    private Timestamp dateposted;
    private Timestamp createdDateTime;
    private Timestamp modifiedDateTime;
    private Byte deleted;
    private BlogCommentEntity blogCommentByBlogpostid;

    @Id
    @Column(name = "blogpostid", nullable = false, insertable = true, updatable = true)
    public int getBlogpostid() {
        return blogpostid;
    }

    public void setBlogpostid(int blogpostid) {
        this.blogpostid = blogpostid;
    }

    @Basic
    @Column(name = "title", nullable = true, insertable = true, updatable = true, length = 2147483647)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "summary", nullable = true, insertable = true, updatable = true, length = 2147483647)
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Basic
    @Column(name = "body", nullable = true, insertable = true, updatable = true, length = 2147483647)
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Basic
    @Column(name = "dateposted", nullable = true, insertable = true, updatable = true)
    public Timestamp getDateposted() {
        return dateposted;
    }

    public void setDateposted(Timestamp dateposted) {
        this.dateposted = dateposted;
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
    @Column(name = "modifiedDateTime", nullable = true, insertable = true, updatable = true)
    public Timestamp getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(Timestamp modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
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

        BlogPostEntity that = (BlogPostEntity) o;

        if (blogpostid != that.blogpostid) return false;
        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        if (createdDateTime != null ? !createdDateTime.equals(that.createdDateTime) : that.createdDateTime != null)
            return false;
        if (dateposted != null ? !dateposted.equals(that.dateposted) : that.dateposted != null) return false;
        if (deleted != null ? !deleted.equals(that.deleted) : that.deleted != null) return false;
        if (modifiedDateTime != null ? !modifiedDateTime.equals(that.modifiedDateTime) : that.modifiedDateTime != null)
            return false;
        if (summary != null ? !summary.equals(that.summary) : that.summary != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = blogpostid;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (dateposted != null ? dateposted.hashCode() : 0);
        result = 31 * result + (createdDateTime != null ? createdDateTime.hashCode() : 0);
        result = 31 * result + (modifiedDateTime != null ? modifiedDateTime.hashCode() : 0);
        result = 31 * result + (deleted != null ? deleted.hashCode() : 0);
        return result;
    }

    @OneToOne(mappedBy = "blogPostByBlogCommentid")
    public BlogCommentEntity getBlogCommentByBlogpostid() {
        return blogCommentByBlogpostid;
    }

    public void setBlogCommentByBlogpostid(BlogCommentEntity blogCommentByBlogpostid) {
        this.blogCommentByBlogpostid = blogCommentByBlogpostid;
    }
}
