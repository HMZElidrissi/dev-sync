package ma.hmzelidrissi.devsync.config;

import jakarta.ejb.Schedule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ma.hmzelidrissi.devsync.entities.Task;
import ma.hmzelidrissi.devsync.entities.TaskChangeRequest;
import ma.hmzelidrissi.devsync.services.TaskService;
import ma.hmzelidrissi.devsync.services.TokenService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ScheduledJobs {
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private TokenService tokenService;

    @Inject
    private TaskService taskService;

    @Schedule(hour = "*", minute = "0") // Every hour
    public void checkAndDoubleTokens() {
        LocalDateTime twelveHoursAgo = LocalDateTime.now().minusHours(12);

        List<TaskChangeRequest> unansweredRequests = entityManager.createQuery(
                        "SELECT tcr FROM TaskChangeRequest tcr WHERE tcr.answered = false AND tcr.requestTime < :twelveHoursAgo",
                        TaskChangeRequest.class)
                .setParameter("twelveHoursAgo", twelveHoursAgo)
                .getResultList();

        for (TaskChangeRequest request : unansweredRequests) {
            tokenService.doubleModificationTokens(request.getRequestor());
        }
    }

    @Schedule(hour = "0", minute = "0") // Midnight
    public void markOverdueTasks() {
        LocalDate today = LocalDate.now();
        List<Task> overdueTasks = taskService.getOverdueTasks(today);

        for (Task task : overdueTasks) {
            task.setCompleted(false);
            taskService.updateTask(task);
        }
    }
}
