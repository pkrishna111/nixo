package com.nixo.service;

import com.nixo.entity.News;
import jakarta.ejb.Stateless;
import jakarta.ejb.LocalBean;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
@LocalBean
public class NewsService {

    @PersistenceContext(unitName = "nixoPU")
    private EntityManager em;

    public void addNews(News news) {
        em.persist(news);
    }

    public List<News> getAllNews() {
        return em.createQuery("SELECT n FROM News n ORDER BY n.createdAt DESC", News.class).getResultList();
    }

    public void deleteNews(int id) {
        News n = em.find(News.class, id);
        if (n != null) {
            em.remove(n);
        }
    }

    public void updateNews(News news) {
        em.merge(news);
    }

    public News findById(int id) {
        return em.find(News.class, id);
    }

    public List<News> searchByTitle(String keyword) {
        return em.createQuery(
                "SELECT n FROM News n WHERE LOWER(n.title) LIKE :kw ORDER BY n.createdAt DESC",
                News.class).setParameter("kw", "%" + keyword.toLowerCase() + "%").getResultList();
    }

    // Simple denormalized increment (no per-user check)
    public void likeNews(int id) {
        News news = em.find(News.class, id);
        if (news != null) {
            news.setLikes(news.getLikes() + 1);
            em.merge(news);
        }
    }

    // Per-user like: inserts into news_likes if not exists and increments news.likes
    public void likeNewsByUser(int newsId, int userId) {
        // 1️⃣ Check if the user already liked this news
        Query exists = em.createNativeQuery("SELECT id FROM news_likes WHERE news_id = ?1 AND user_id = ?2");
        exists.setParameter(1, newsId);
        exists.setParameter(2, userId);
        List<?> result = exists.getResultList();

        News news = em.find(News.class, newsId);
        if (news == null) {
            return; // safety check
        }
        if (result.isEmpty()) {
            // 2️⃣ User has not liked —> INSERT like
            Query insert = em.createNativeQuery("INSERT INTO news_likes (news_id, user_id) VALUES (?1, ?2)");
            insert.setParameter(1, newsId);
            insert.setParameter(2, userId);
            insert.executeUpdate();

            // Increment total likes
            news.setLikes(news.getLikes() + 1);
            em.merge(news);
        } else {
            // 3️⃣ User already liked —> REMOVE like (toggle off)
            Query delete = em.createNativeQuery("DELETE FROM news_likes WHERE news_id = ?1 AND user_id = ?2");
            delete.setParameter(1, newsId);
            delete.setParameter(2, userId);
            delete.executeUpdate();

            // Decrement total likes (prevent negative)
            int currentLikes = news.getLikes();
            news.setLikes(currentLikes > 0 ? currentLikes - 1 : 0);

            em.merge(news);
        }
    }

   

}
