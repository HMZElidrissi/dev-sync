package ma.hmzelidrissi.devsync.daos.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ma.hmzelidrissi.devsync.daos.TokenDAO;
import ma.hmzelidrissi.devsync.entities.Token;
import ma.hmzelidrissi.devsync.entities.User;

import java.util.List;

@ApplicationScoped
@Transactional
public class TokenDAOImpl implements TokenDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Token create(Token token) {
        entityManager.persist(token);
        return token;
    }

    @Override
    public Token findByUser(User user) {
        return entityManager.createQuery("SELECT t FROM Token t WHERE t.user = :user", Token.class)
                .setParameter("user", user)
                .getSingleResult();
    }

    @Override
    public List<Token> findAll() {
        return entityManager.createQuery("SELECT t FROM Token t", Token.class).getResultList();
    }

    @Override
    public Token update(Token token) {
        return entityManager.merge(token);
    }

    @Override
    public void delete(Long id) {
        Token token = entityManager.find(Token.class, id);
        if (token != null) {
            entityManager.remove(token);
        }
    }
}
