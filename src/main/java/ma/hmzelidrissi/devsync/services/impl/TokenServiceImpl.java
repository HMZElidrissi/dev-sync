package ma.hmzelidrissi.devsync.services.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ma.hmzelidrissi.devsync.daos.TokenDAO;
import ma.hmzelidrissi.devsync.entities.Token;
import ma.hmzelidrissi.devsync.entities.User;
import ma.hmzelidrissi.devsync.services.TokenService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Transactional
public class TokenServiceImpl implements TokenService {
    @Inject
    private TokenDAO tokenDAO;

    private static final Logger LOGGER = Logger.getLogger(TokenServiceImpl.class.getName());

    @Override
    public void useReplaceToken(User user) {
        Token token = tokenDAO.findByUser(user);
        if (token.getDailyReplaceTokens() > 0) {
            token.setDailyReplaceTokens(token.getDailyReplaceTokens() - 1);
            tokenDAO.update(token);
        } else {
            throw new IllegalStateException("No replace tokens available");
        }
    }

    @Override
    public void useDeleteToken(User user) {
        LOGGER.log(Level.INFO, "Attempting to use delete token for user: {0}", user.getUsername());
        try {
            Token token = tokenDAO.findByUser(user);
            if (token == null) {
                LOGGER.log(Level.SEVERE, "No token found for user: {0}", user.getUsername());
                throw new IllegalStateException("No token found for user");
            }
            LOGGER.log(Level.INFO, "Current monthly delete tokens for user {0}: {1}", new Object[]{user.getUsername(), token.getMonthlyDeleteTokens()});
            if (token.getMonthlyDeleteTokens() > 0) {
                token.setMonthlyDeleteTokens(token.getMonthlyDeleteTokens() - 1);
                tokenDAO.update(token);
                LOGGER.log(Level.INFO, "Delete token used successfully. Remaining tokens: {0}", token.getMonthlyDeleteTokens());
            } else {
                LOGGER.log(Level.WARNING, "No delete tokens available for user: {0}", user.getUsername());
                throw new IllegalStateException("No delete tokens available");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error using delete token for user " + user.getUsername() + ": " + e.getMessage(), e);
            throw new RuntimeException("Error using delete token", e);
        }
    }

    @Override
    public void resetDailyTokens() {
        List<Token> tokens = tokenDAO.findAll();
        for (Token token : tokens) {
            token.setDailyReplaceTokens(2);
            tokenDAO.update(token);
        }
    }

    @Override
    public void resetMonthlyTokens() {
        List<Token> tokens = tokenDAO.findAll();
        for (Token token : tokens) {
            token.setMonthlyDeleteTokens(1);
            tokenDAO.update(token);
        }
    }
}
