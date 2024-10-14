<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<t:layout title="Your Tasks">
    <div class="container mx-auto px-4">
        <h1 class="text-2xl font-bold mb-4">Your Tasks</h1>

        <c:if test="${empty tasks}">
            <p>You have no tasks.</p>
        </c:if>

        <c:if test="${not empty tasks}">
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                <c:forEach var="task" items="${tasks}">
                    <div class="bg-white shadow-md rounded-lg p-4">
                        <h2 class="text-xl font-semibold mb-2">${task.title}</h2>
                        <p class="text-gray-600 mb-2">${task.description}</p>
                        <p class="text-sm text-gray-500 mb-2">
                            Due:
                            <c:choose>
                                <c:when test="${task.dueDate != null}">
                                    ${task.dueDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))}
                                    <c:if test="${task.dueDate.isBefore(LocalDate.now())}">
                                        <span class="text-red-500">(Overdue)</span>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-yellow-500">No due date set</span>
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <div class="flex flex-wrap gap-2 mb-2">
                            <c:forEach var="tag" items="${task.tags}">
                                <span class="bg-blue-100 text-blue-800 text-xs font-semibold px-2.5 py-0.5 rounded">${tag}</span>
                            </c:forEach>
                        </div>
                        <div class="flex justify-between items-center">
                            <a href="${pageContext.request.contextPath}/user/tasks/view/${task.id}" class="text-blue-600 hover:text-blue-800">View Details</a>
                            <c:if test="${not task.completed}">
                                <form action="${pageContext.request.contextPath}/user/tasks/status/${task.id}" method="POST">
                                    <input type="hidden" name="completed" value="true">
                                    <button type="submit" class="bg-green-500 hover:bg-green-600 text-white font-bold py-1 px-2 rounded">
                                        Mark as Completed
                                    </button>
                                </form>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <div class="mt-4">
            <a href="${pageContext.request.contextPath}/user/tasks/new" class="bg-gray-700 hover:bg-gray-900 text-white font-bold py-2 px-4 rounded">
                Create New Task
            </a>
        </div>
    </div>
</t:layout>

<script>
    function markAsCompleted(taskId) {
        fetch('${pageContext.request.contextPath}/user/tasks/status/' + taskId, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'completed=true'
        }).then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Error marking task as completed');
            }
        });
    }

    function deleteTask(taskId) {
        if (confirm('Are you sure you want to delete this task?')) {
            fetch('${pageContext.request.contextPath}/user/tasks/' + taskId, {
                method: 'DELETE',
            }).then(response => {
                if (response.ok) {
                    location.reload();
                } else {
                    alert('Error deleting task');
                }
            });
        }
    }

    function deleteTaskWithToken(taskId) {
        if (confirm('Are you sure you want to delete this task? This will use one of your monthly delete tokens.')) {
            fetch('${pageContext.request.contextPath}/user/tasks/' + taskId + '?useToken=true', {
                method: 'DELETE',
            }).then(response => {
                if (response.ok) {
                    location.reload();
                } else {
                    alert('Error deleting task');
                }
            });
        }
    }
</script>
