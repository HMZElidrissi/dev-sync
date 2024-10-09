<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="View Task">
    <div class="bg-white shadow-md rounded-lg overflow-hidden">
        <div class="px-6 py-4">
            <h2 class="text-2xl font-bold mb-2">${task.title}</h2>
            <p class="text-gray-700 text-base mb-4">${task.description}</p>
            <div class="grid grid-cols-2 gap-4">
                <div>
                    <p class="text-sm text-gray-600">Due Date:</p>
                    <p class="font-semibold">${task.dueDate}</p>
                </div>
                <div>
                    <p class="text-sm text-gray-600">Status:</p>
                    <p class="font-semibold ${task.completed ? 'text-green-600' : 'text-red-600'}">
                            ${task.completed ? 'Completed' : 'Pending'}
                    </p>
                </div>
                <div>
                    <p class="text-sm text-gray-600">Assigned To:</p>
                    <p class="font-semibold">${task.assignedTo.username}</p>
                </div>
                <div>
                    <p class="text-sm text-gray-600">Created By:</p>
                    <p class="font-semibold">${task.createdBy.username}</p>
                </div>
            </div>
            <div class="mt-4">
                <p class="text-sm text-gray-600">Tags:</p>
                <div class="flex flex-wrap gap-2 mt-1">
                    <c:forEach var="tag" items="${task.tags}">
                        <span class="inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700">
                                ${tag}
                        </span>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="px-6 py-4 bg-gray-50 border-t border-gray-200">
            <a href="${pageContext.request.contextPath}/tasks/edit/${task.id}" class="inline-block bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mr-2">
                Edit Task
            </a>
            <c:if test="${!task.completed}">
                <button onclick="markAsCompleted(${task.id})" class="inline-block bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded mr-2">
                    Mark as Completed
                </button>
            </c:if>
            <button onclick="deleteTask(${task.id})" class="inline-block bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded mr-2">
                Delete Task
            </button>
            <a href="${pageContext.request.contextPath}/tasks" class="inline-block bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded">
                Back to List
            </a>
        </div>
    </div>

    <script>
        function markAsCompleted(taskId) {
            if (confirm('Are you sure you want to mark this task as completed?')) {
                updateTaskStatus(taskId, true);
            }
        }

        function deleteTask(taskId) {
            if (confirm('Are you sure you want to delete this task? This action may use a token.')) {
                fetch('${pageContext.request.contextPath}/tasks/' + taskId, {
                    method: 'DELETE'
                })
                    .then(response => {
                        if (response.ok) {
                            window.location.href = '${pageContext.request.contextPath}/tasks';
                        } else {
                            alert('Failed to delete task');
                        }
                    });
            }
        }

        function updateTaskStatus(taskId, completed) {
            fetch('${pageContext.request.contextPath}/tasks/' + taskId + '/status', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'completed=' + completed
            })
                .then(response => {
                    if (response.ok) {
                        window.location.reload();
                    } else {
                        alert('Failed to update task status');
                    }
                });
        }
    </script>
</t:layout>
