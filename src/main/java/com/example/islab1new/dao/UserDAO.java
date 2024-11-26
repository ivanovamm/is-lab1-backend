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
            throw e;
        }
    }


    public User findById(Integer id) {
        return em.find(User.class, id);

    }

//    public User findUserByName(String name) {
//        return em.find(User.class, name);
//
//    }


    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();

    }

    @Transactional
    public void update(User user) {
        try {
            em.merge(user);

        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void delete(Integer id) {
        try {
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
            } else {
                throw new EntityNotFoundException("User not found");
            }
        } catch (Exception e) {
            throw e;
        }
    }


    public Optional<User> findByUsername(String username) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        return query.getResultStream().findFirst();
    }

    public User findUserByName(String name) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :name", User.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
