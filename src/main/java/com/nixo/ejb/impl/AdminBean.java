package com.nixo.ejb.impl;


import com.nixo.entity.RoleMaster;
import com.nixo.entity.SharerRequests;
import com.nixo.entity.Users;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class AdminBean implements AdminBeanLocal {

    @PersistenceContext(unitName = "NixoPU")
    private EntityManager em;

    @Override
    public List<SharerRequests> getPendingRequests() {
        return em.createQuery(
                "SELECT s FROM SharerRequests s WHERE s.status = :st",
                SharerRequests.class)
                .setParameter("st", "pending")
                .getResultList();
    }

    @Override
    public void approveRequest(int requestId) {
        SharerRequests req = em.find(SharerRequests.class, requestId);
        if (req == null) return;

        req.setStatus("approved");
        Users u = req.getUserId();

        RoleMaster sharerRole = em.find(RoleMaster.class, 2); // role_id = 2
        u.setRoleId(sharerRole);

        em.merge(req);
        em.merge(u);
    }

    @Override
    public void rejectRequest(int requestId) {
        SharerRequests req = em.find(SharerRequests.class, requestId);
        if (req == null) return;

        req.setStatus("rejected");
        em.merge(req);
    }

    @Override
    public void changeUserRole(int userId, int newRoleId) {
        Users u = em.find(Users.class, userId);
        RoleMaster r = em.find(RoleMaster.class, newRoleId);

        if (u != null && r != null) {
            u.setRoleId(r);
            em.merge(u);
        }
    }
}
