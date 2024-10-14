package ma.hmzelidrissi.devsync.services;

import ma.hmzelidrissi.devsync.entities.User;

public interface TokenService {
    void useReplaceToken(User user);
    void useDeleteToken(User user);
    void resetDailyTokens();
    void resetMonthlyTokens();
    void doubleModificationTokens(User requestor);

    int getTokensUsed(Long userId);
}
