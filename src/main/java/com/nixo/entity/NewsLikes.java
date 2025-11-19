/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nixo.entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Kyara Infotech 01
 */
@Entity
@Table(name = "news_likes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NewsLikes.findAll", query = "SELECT n FROM NewsLikes n"),
    @NamedQuery(name = "NewsLikes.findById", query = "SELECT n FROM NewsLikes n WHERE n.id = :id"),
    @NamedQuery(name = "NewsLikes.findByCreatedAt", query = "SELECT n FROM NewsLikes n WHERE n.createdAt = :createdAt")})
public class NewsLikes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "news_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private News newsId;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private Users userId;

    public NewsLikes() {
    }

    public NewsLikes(Integer id) {
        this.id = id;
    }

    public NewsLikes(Integer id, Date createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @JsonbTransient
    public News getNewsId() {
        return newsId;
    }

    public void setNewsId(News newsId) {
        this.newsId = newsId;
    }

    @JsonbTransient
    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NewsLikes)) {
            return false;
        }
        NewsLikes other = (NewsLikes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nixo.entity.NewsLikes[ id=" + id + " ]";
    }

}
