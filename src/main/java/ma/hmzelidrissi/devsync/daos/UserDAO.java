package ma.hmzelidrissi.devsync.daos;

import ma.hmzelidrissi.devsync.entities.User;
import java.util.List;

public interface UserDAO {
    User create(User user);
    User findById(Long id);
    List<User> findAll();
    User update(User user);
    void delete(Long id);
}