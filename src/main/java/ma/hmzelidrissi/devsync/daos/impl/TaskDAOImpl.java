package ma.hmzelidrissi.devsync.daos.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ma.hmzelidrissi.devsync.daos.TaskDAO;
import ma.hmzelidrissi.devsync.entities.Task;

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
}
