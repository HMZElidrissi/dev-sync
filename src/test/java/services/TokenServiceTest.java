package services;

import ma.hmzelidrissi.devsync.daos.TokenDAO;
import ma.hmzelidrissi.devsync.entities.Token;
import ma.hmzelidrissi.devsync.entities.User;
import ma.hmzelidrissi.devsync.services.UserService;
import ma.hmzelidrissi.devsync.services.impl.TokenServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private TokenDAO tokenDAO;

    @Mock
    private UserService userService;

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Test
    void testUseReplaceToken() {
        User user = new User();
        Token token = new Token();
        token.setDailyReplaceTokens(2);

        when(tokenDAO.findByUser(user)).thenReturn(token);

        tokenService.useReplaceToken(user);

        assertEquals(1, token.getDailyReplaceTokens());
        verify(tokenDAO).update(token);
    }

    @Test
    void testUseReplaceTokenNoTokensAvailable() {
        User user = new User();
        Token token = new Token();
        token.setDailyReplaceTokens(0);

        when(tokenDAO.findByUser(user)).thenReturn(token);

        assertThrows(IllegalStateException.class, () -> tokenService.useReplaceToken(user));
    }

    @Test
    void testUseDeleteToken() {
        User user = new User();
        Token token = new Token();
        token.setMonthlyDeleteTokens(1);

        when(tokenDAO.findByUser(user)).thenReturn(token);

        tokenService.useDeleteToken(user);

        assertEquals(0, token.getMonthlyDeleteTokens());
        verify(tokenDAO).update(token);
    }

    @Test
    void testResetDailyTokens() {
        Token token1 = new Token();
        Token token2 = new Token();
        when(tokenDAO.findAll()).thenReturn(Arrays.asList(token1, token2));

        tokenService.resetDailyTokens();

        assertEquals(2, token1.getDailyReplaceTokens());
        assertEquals(2, token2.getDailyReplaceTokens());
        verify(tokenDAO, times(2)).update(any(Token.class));
    }

    @Test
    void testGetTokensUsed() {
        Long userId = 1L;
        User user = new User();
        Token token = new Token();
        token.setDailyReplaceTokens(1);
        token.setMonthlyDeleteTokens(0);
        user.setToken(token);

        when(userService.getUserById(userId)).thenReturn(user);

        int tokensUsed = tokenService.getTokensUsed(userId);

        assertEquals(2, tokensUsed);
    }
}