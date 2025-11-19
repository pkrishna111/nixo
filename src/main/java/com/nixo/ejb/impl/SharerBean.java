package com.nixo.ejb.impl;


import com.nixo.entity.News;
import com.nixo.entity.Users;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Stateless
public class SharerBean implements SharerBeanLocal {

    @PersistenceContext(unitName = "NixoPU")
    private EntityManager em;

    @Override
    public void addNews(int sharerUserId, String title, String content, String imageUrl) {
        Users sharer = em.find(Users.class, sharerUserId);

        News n = new News();
        n.setTitle(title);
        n.setContent(content);
        n.setImageUrl(imageUrl);
        n.setCreatedAt(new Date());
        n.setLikes(0);
        n.setStatus("pending");
        n.setSharerId(sharer);

        em.persist(n);
    }

    @Override
    public void editNews(int newsId, String title, String content, String imageUrl) {
        News n = em.find(News.class, newsId);
        if (n == null) return;

        n.setTitle(title);
        n.setContent(content);
        n.setImageUrl(imageUrl);

        em.merge(n);
    }

    @Override
    public void deleteNews(int newsId) {
        News n = em.find(News.class, newsId);
        if (n != null) em.remove(n);
    }

    @Override
    public List<News> getSharerNews(int sharerUserId) {
        return em.createQuery(
                "SELECT n FROM News n WHERE n.sharerId.userId = :uid",
                News.class)
                .setParameter("uid", sharerUserId)
                .getResultList();
    }
}
