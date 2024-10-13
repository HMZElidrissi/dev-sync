package ma.hmzelidrissi.devsync.daos;

import ma.hmzelidrissi.devsync.entities.Task;

import java.util.List;

public interface TaskDAO {
    Task create(Task task);
    Task findById(Long id);
    List<Task> findAll();
    Task update(Task task);
    void delete(Long id);
    List<Task> findByUserId(Long id);
}
