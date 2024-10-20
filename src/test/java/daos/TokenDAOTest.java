package daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ma.hmzelidrissi.devsync.daos.impl.TokenDAOImpl;
import ma.hmzelidrissi.devsync.entities.Token;
import ma.hmzelidrissi.devsync.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenDAOTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Token> typedQuery;

    @InjectMocks
    private TokenDAOImpl tokenDAO;

    private Token testToken;

    @BeforeEach
    void setUp() {
        testToken = new Token();
        testToken.setId(1L);
        testToken.setUser(new User());
    }

    @Test
    void create_shouldPersistToken() {
        tokenDAO.create(testToken);
        verify(entityManager).persist(testToken);
    }

    @Test
    void findByUser_shouldReturnToken() {
        when(entityManager.createQuery("SELECT t FROM Token t WHERE t.user = :user", Token.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("user", testToken.getUser())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(testToken);

        Token token = tokenDAO.findByUser(testToken.getUser());

        assertEquals(testToken, token);
    }

    @Test
    void delete_shouldRemoveToken() {
        when(entityManager.find(Token.class, 1L)).thenReturn(testToken);

        tokenDAO.delete(1L);

        verify(entityManager).remove(testToken);
    }

    @Test
    void delete_shouldNotRemoveToken() {
        when(entityManager.find(Token.class, 1L)).thenReturn(null);

        tokenDAO.delete(1L);

        verify(entityManager, never()).remove(any(Token.class));
    }

    @Test
    void findAll_shouldReturnTokens() {
        List<Token> tokens = List.of(testToken, new Token());
        when(entityManager.createQuery("SELECT t FROM Token t", Token.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(tokens);

        List<Token> results = tokenDAO.findAll();

        assertEquals(2, results.size());
        assertEquals(tokens, results);
    }
}
