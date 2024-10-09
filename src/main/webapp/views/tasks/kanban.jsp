<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="Task Kanban Board">
    <div class="flex space-x-4 overflow-x-auto pb-4">
        <div class="flex-1 min-w-[300px]">
            <h2 class="text-lg font-semibold mb-2">To Do</h2>
            <div class="bg-gray-100 p-4 rounded-lg min-h-[500px]">
                <c:forEach var="task" items="${tasks}">
                    <c:if test="${!task.completed}">
                        <div class="bg-white p-4 mb-4 rounded shadow">
                            <h3 class="font-semibold">${task.title}</h3>
                            <p class="text-sm text-gray-600">${task.description}</p>
                            <div class="mt-2">
                                <span class="text-xs text-gray-500">Due: ${task.dueDate}</span>
                                <span class="text-xs text-gray-500 ml-2">Assigned to: ${task.assignedTo.username}</span>
                            </div>
                            <div class="mt-2 flex flex-wrap">
                                <c:forEach var="tag" items="${task.tags}">
                                    <span class="bg-blue-100 text-blue-800 text-xs font-semibold mr-2 px-2.5 py-0.5 rounded">${tag}</span>
                                </c:forEach>
                            </div>
                            <div class="mt-2 flex justify-between items-center">
                                <button onclick="updateTaskStatus(${task.id}, true)" class="text-sm text-blue-600 hover:text-blue-800">Mark Complete</button>
                                <button onclick="showReplaceForm(${task.id})" class="text-sm text-green-600 hover:text-green-800">Replace</button>
                                <button onclick="deleteTask(${task.id})" class="text-sm text-red-600 hover:text-red-800">Delete</button>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
        <div class="flex-1 min-w-[300px]">
            <h2 class="text-lg font-semibold mb-2">Completed</h2>
            <div class="bg-gray-100 p-4 rounded-lg min-h-[500px]">
                <c:forEach var="task" items="${tasks}">
                    <c:if test="${task.completed}">
                        <div class="bg-white p-4 mb-4 rounded shadow">
                            <h3 class="font-semibold">${task.title}</h3>
                            <p class="text-sm text-gray-600">${task.description}</p>
                            <div class="mt-2">
                                <span class="text-xs text-gray-500">Completed</span>
                                <span class="text-xs text-gray-500 ml-2">Assigned to: ${task.assignedTo.username}</span>
                            </div>
                            <div class="mt-2 flex flex-wrap">
                                <c:forEach var="tag" items="${task.tags}">
                                    <span class="bg-blue-100 text-blue-800 text-xs font-semibold mr-2 px-2.5 py-0.5 rounded">${tag}</span>
                                </c:forEach>
                            </div>
                            <div class="mt-2 flex justify-between items-center">
                                <button onclick="updateTaskStatus(${task.id}, false)" class="text-sm text-blue-600 hover:text-blue-800">Reopen</button>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </div>

    <script>
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

        function showReplaceForm(taskId) {
            // Implement a modal or redirect to a replace form
            window.location.href = '${pageContext.request.contextPath}/tasks/edit/' + taskId;
        }

        function deleteTask(taskId) {
            if (confirm('Are you sure you want to delete this task? This action may use a token.')) {
                fetch('${pageContext.request.contextPath}/tasks/' + taskId, {
                    method: 'DELETE'
                })
                    .then(response => {
                        if (response.ok) {
                            window.location.reload();
                        } else {
                            alert('Failed to delete task');
                        }
                    });
            }
        }
    </script>
</t:layout>
