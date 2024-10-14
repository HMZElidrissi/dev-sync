package ma.hmzelidrissi.devsync.daos.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ma.hmzelidrissi.devsync.daos.TaskDAO;
import ma.hmzelidrissi.devsync.entities.Task;
import ma.hmzelidrissi.devsync.entities.TaskChangeRequest;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
@Transactional
public class TaskDAOImpl implements TaskDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Task create(Task task) {
        entityManager.persist(task);
        return task;
    }

    @Override
    public Task findById(Long id) {
        return entityManager.find(Task.class, id);
    }

    @Override
    public List<Task> findAll() {
        return entityManager.createQuery("SELECT t from Task t", Task.class).getResultList();
    }

    @Override
    public Task update(Task task) {
        return entityManager.merge(task);
    }

    @Override
    public void delete(Long id) {
        Task task = entityManager.find(Task.class, id);
        if (task != null){
         entityManager.remove(task);
        }
    }

    @Override
    public List<Task> findByUserId(Long id) {
        return entityManager.createQuery("SELECT t from Task t WHERE t.assignedTo.id = :id", Task.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<Task> findOverdueTasks(LocalDate today) {
        return entityManager.createQuery("SELECT t from Task t WHERE t.dueDate < :today AND t.completed = false", Task.class)
                .setParameter("today", today)
                .getResultList();
    }

    @Override
    public List<Task> findByAssignedUser(Long userId) {
        return entityManager.createQuery(
                        "SELECT t FROM Task t WHERE t.assignedTo.id = :userId", Task.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public void createTaskChangeRequest(TaskChangeRequest request) {
        entityManager.persist(request);
    }
}
