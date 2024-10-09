package ma.hmzelidrissi.devsync.daos;

import ma.hmzelidrissi.devsync.entities.Token;
import ma.hmzelidrissi.devsync.entities.User;

import java.util.List;

public interface TokenDAO {
    Token create(Token token);
    Token findByUser(User user);
    List<Token> findAll();
    Token update(Token token);
    void delete(Long id);
}
