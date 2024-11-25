package com.example.islab1new.dao;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;
import com.example.islab1new.models.User;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Named
@RequestScoped
public class UserDAO {

  @PersistenceContext
  private EntityManager em;

  @Transactional
    public void save(User user) {
        try {
            em.persist(user);
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public User findById(Integer id) {
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }



    public List<User> findAll() {
        try {
            return em.createQuery("SELECT u FROM User u", User.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void update(User user) {
        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(Integer id) {
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
            } else {
                throw new EntityNotFoundException("User not found");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }


    public Optional<User> findByUsername(String username) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        return query.getResultStream().findFirst();
    }
}
