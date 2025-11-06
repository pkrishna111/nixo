package com.nixo.service;

import com.nixo.entity.SharerRequest;
import com.nixo.entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class SharerRequestService {

    @PersistenceContext(unitName = "nixoPU")
    private EntityManager em;

    public void submitRequest(User user, String reason) {
        SharerRequest req = new SharerRequest(user, reason);
        em.persist(req);
    }

    public List<SharerRequest> getAllRequests() {
        return em.createQuery("SELECT r FROM SharerRequest r ORDER BY r.createdAt DESC", SharerRequest.class)
                .getResultList();
    }

    public List<SharerRequest> getPendingRequests() {
        return em.createQuery("SELECT r FROM SharerRequest r WHERE r.status = 'pending' ORDER BY r.createdAt DESC",
                SharerRequest.class).getResultList();
    }

    public void updateStatus(int id, String status) {
        SharerRequest req = em.find(SharerRequest.class, id);
        if (req != null) {
            req.setStatus(status);
            em.merge(req);
        }
    }

    public SharerRequest findById(int id) {
        return em.find(SharerRequest.class, id);
    }

    public boolean hasPendingRequest(User user) {
        List<SharerRequest> list = em.createQuery(
                "SELECT r FROM SharerRequest r WHERE r.user = :user AND r.status = 'pending'", SharerRequest.class)
                .setParameter("user", user)
                .getResultList();
        return !list.isEmpty();
    }

    public String getRequestStatusByUser(User user) {
        try {
            SharerRequest req = em.createQuery(
                    "SELECT r FROM SharerRequest r WHERE r.user = :user ORDER BY r.createdAt DESC",
                    SharerRequest.class)
                    .setParameter("user", user)
                    .setMaxResults(1)
                    .getSingleResult();

            return req.getStatus();
        } catch (Exception e) {
            return null; // No request found
        }
    }

}
