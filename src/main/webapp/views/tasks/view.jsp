<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="View Task">
    <div class="bg-white shadow-md rounded-lg p-6">
        <h2 class="text-2xl font-bold mb-4">${task.title}</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
                <p class="mb-2"><strong>Description:</strong> ${task.description}</p>
                <p class="mb-2"><strong>Due Date:</strong> ${task.dueDate}</p>
                <p class="mb-2"><strong>Status:</strong>
                    <c:choose>
                        <c:when test="${task.completed}">
                            <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">Completed</span>
                        </c:when>
                        <c:otherwise>
                            <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-yellow-100 text-yellow-800">Pending</span>
                        </c:otherwise>
                    </c:choose>
                </p>
                <p class="mb-2"><strong>Tags:</strong></p>
                <div class="flex flex-wrap gap-2 mb-2">
                    <c:forEach var="tag" items="${task.tags}">
                        <span class="bg-blue-100 text-blue-800 text-xs font-semibold px-2.5 py-0.5 rounded">${tag}</span>
                    </c:forEach>
                </div>
            </div>
            <div>
                <p class="mb-2"><strong>Assigned To:</strong> ${task.assignedTo.username}</p>
                <p class="mb-2"><strong>Created By:</strong> ${task.createdBy.username}</p>
                <p class="mb-2"><strong>Created At:</strong> ${task.createdAt}</p>
            </div>
        </div>
        <div class="mt-6 space-x-2">
            <c:choose>
                <c:when test="${sessionScope.user.role == 'MANAGER'}">
                    <a href="${pageContext.request.contextPath}/admin/tasks/edit/${task.id}" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">Edit</a>
                    <a href="#" onclick="deleteTask(${task.id})" class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded">Delete</a>
                    <a href="${pageContext.request.contextPath}/admin/tasks" class="bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded">Back to List</a>
                </c:when>
                <c:otherwise>
                    <c:if test="${!task.completed && task.assignedTo.id == sessionScope.user.id}">
                        <a href="#" onclick="markAsCompleted(${task.id})" class="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">Mark as Completed</a>
                    </c:if>
                    <c:if test="${task.createdBy.id == sessionScope.user.id}">
                        <a href="${pageContext.request.contextPath}/user/tasks/edit/${task.id}" class="bg-gray-700 hover:bg-gray-900 text-white font-bold py-2 px-4 rounded">Edit</a>
                    </c:if>
                    <c:if test="${task.assignedTo.id == sessionScope.user.id}">
                        <a href="#" onclick="deleteTask(${task.id})" class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded">Delete</a>
                    </c:if>
                    <c:if test="${task.createdBy.id != sessionScope.user.id && task.assignedTo.id == sessionScope.user.id}">
                        <a href="${pageContext.request.contextPath}/user/tasks/replace/${task.id}" class="bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded">Request Change</a>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/user/tasks" class="bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded">Back to List</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</t:layout>

<script>
    function markAsCompleted(taskId) {
        event.preventDefault();
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
            fetch('${pageContext.request.contextPath}/${sessionScope.user.role == 'MANAGER' ? 'admin' : 'user'}/tasks/' + taskId, {
                method: 'DELETE',
            }).then(response => {
                if (response.ok) {
                    window.location.href = '${pageContext.request.contextPath}/${sessionScope.user.role == 'MANAGER' ? 'admin' : 'user'}/tasks';
                } else {
                    alert('Error deleting task');
                }
            });
        }
    }
</script>