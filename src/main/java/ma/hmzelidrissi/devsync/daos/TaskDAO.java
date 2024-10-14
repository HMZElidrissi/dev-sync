package ma.hmzelidrissi.devsync.daos;

import ma.hmzelidrissi.devsync.entities.Task;
import ma.hmzelidrissi.devsync.entities.TaskChangeRequest;

import java.time.LocalDate;
import java.util.List;

public interface TaskDAO {
    Task create(Task task);
    Task findById(Long id);
    List<Task> findAll();
    Task update(Task task);
    void delete(Long id);
    List<Task> findByUserId(Long id);
    List<Task> findOverdueTasks(LocalDate today);
    List<Task> findByAssignedUser(Long userId);
    void createTaskChangeRequest(TaskChangeRequest request);
}
