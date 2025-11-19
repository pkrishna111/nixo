package com.nixo.ejb.impl;


import com.nixo.entity.News;
import com.nixo.entity.NewsLikes;
import com.nixo.entity.Users;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Stateless
public class ReceiverBean implements ReceiverBeanLocal {

    @PersistenceContext(unitName = "NixoPU")
    private EntityManager em;

    @Override
    public List<News> getAllNews() {
        return em.createQuery("SELECT n FROM News n ORDER BY n.createdAt DESC", News.class)
                .getResultList();
    }

    @Override
    public List<News> searchNews(String keyword) {
        return em.createQuery(
                "SELECT n FROM News n WHERE LOWER(n.title) LIKE :kw", News.class)
                .setParameter("kw", "%" + keyword.toLowerCase() + "%")
                .getResultList();
    }

    @Override
    public boolean toggleLike(int userId, int newsId) {
        Users user = em.find(Users.class, userId);
        News news = em.find(News.class, newsId);

        List<NewsLikes> existing = em.createQuery(
                "SELECT nl FROM NewsLikes nl WHERE nl.userId.userId = :uid AND nl.newsId.id = :nid",
                NewsLikes.class)
                .setParameter("uid", userId)
                .setParameter("nid", newsId)
                .getResultList();

        if (!existing.isEmpty()) {
            // remove like
            em.remove(existing.get(0));
            news.setLikes(news.getLikes() - 1);
            em.merge(news);
            return false; // unliked
        }

        // add like
        NewsLikes nl = new NewsLikes();
        nl.setCreatedAt(new Date());
        nl.setUserId(user);
        nl.setNewsId(news);

        em.persist(nl);

        news.setLikes(news.getLikes() + 1);
        em.merge(news);

        return true;
    }

    @Override
    public int getUserLikeCountOnNews(int userId, int newsId) {
        return em.createQuery(
                "SELECT COUNT(nl) FROM NewsLikes nl WHERE nl.userId.userId = :uid AND nl.newsId.id = :nid",
                Long.class)
                .setParameter("uid", userId)
                .setParameter("nid", newsId)
                .getSingleResult().intValue();
    }
}
