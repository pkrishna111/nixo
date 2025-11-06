package com.nixo.service;

import com.nixo.entity.User;
import com.nixo.entity.Role;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class UserService {

    @PersistenceContext(unitName = "nixoPU")
    private EntityManager em;

    // Register new user - ensure role is managed
    public void registerUser(User user) {
        // If role is provided by caller by id or name, resolve to managed Role
        Role r = user.getRole();
        if (r != null) {
            if (r.getRoleId() != 0) {
                Role managed = em.find(Role.class, r.getRoleId());
                user.setRole(managed);
            } else if (r.getRoleName() != null) {
                Role managed = findRoleByName(r.getRoleName());
                if (managed != null) user.setRole(managed);
            }
        }
        em.persist(user);
    }

    // Find user by email (used for login)
    public User findByEmail(String email) {
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    // Authenticate user (plain example)
    public User login(String email, String password) {
        User user = findByEmail(email);
        if (user != null && user.getPassword().equals(password) && "active".equals(user.getStatus())) {
            return user;
        }
        return null;
    }

    // Get all users
    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    // Change role or status
    public void updateUser(User user) {
        em.merge(user);
    }

    // Helper to find role by name
    public Role findRoleByName(String roleName) {
        try {
            TypedQuery<Role> q = em.createQuery("SELECT r FROM Role r WHERE r.roleName = :name", Role.class);
            q.setParameter("name", roleName);
            return q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public void updateUserRole(int userId, String roleName) {
    User user = em.find(User.class, userId);
    if (user != null) {
        Role newRole = em.createQuery("SELECT r FROM Role r WHERE r.roleName = :name", Role.class)
                         .setParameter("name", roleName)
                         .getSingleResult();
        user.setRole(newRole);
        em.merge(user);
    }
}

}
