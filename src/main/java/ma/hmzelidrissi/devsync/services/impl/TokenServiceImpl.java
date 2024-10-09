package ma.hmzelidrissi.devsync.services.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ma.hmzelidrissi.devsync.daos.TokenDAO;
import ma.hmzelidrissi.devsync.entities.Token;
import ma.hmzelidrissi.devsync.entities.User;
import ma.hmzelidrissi.devsync.services.TokenService;

import java.util.List;

@ApplicationScoped
@Transactional
public class TokenServiceImpl implements TokenService {
    @Inject
    private TokenDAO tokenDAO;

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
        Token token = tokenDAO.findByUser(user);
        if (token.getMonthlyDeleteTokens() > 0) {
            token.setMonthlyDeleteTokens(token.getMonthlyDeleteTokens() - 1);
            tokenDAO.update(token);
        } else {
            throw new IllegalStateException("No delete tokens available");
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
