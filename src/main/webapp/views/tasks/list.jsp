<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="Task Management">
    <div class="bg-white shadow-md rounded-lg overflow-hidden">
        <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
            <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Title</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Due Date</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Assigned To</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
            </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
            <c:forEach var="task" items="${tasks}">
                <tr>
                    <td class="px-6 py-4 whitespace-nowrap">${task.title}</td>
                    <td class="px-6 py-4 whitespace-nowrap">${task.dueDate}</td>
                    <td class="px-6 py-4 whitespace-nowrap">${task.assignedTo.username}</td>
                    <td class="px-6 py-4 whitespace-nowrap">
                        <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${task.completed ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}">
                                ${task.completed ? 'Completed' : 'Pending'}
                        </span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                        <a href="${pageContext.request.contextPath}/tasks/${task.id}" class="text-indigo-600 hover:text-indigo-900">View</a>
                        <a href="${pageContext.request.contextPath}/tasks/edit/${task.id}" class="text-blue-600 hover:text-blue-900">Edit</a>
                        <a href="#" onclick="deleteTask(${task.id})" class="text-red-600 hover:text-red-900">Delete</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="mt-6">
        <a href="${pageContext.request.contextPath}/tasks/new" class="inline-block bg-gray-700 hover:bg-gray-900 text-white font-bold py-2 px-4 rounded">
            Create New Task
        </a>
    </div>

    <script>
        function deleteTask(taskId) {
            if (confirm('Are you sure you want to delete this task? This action may use a token.')) {
                fetch('${pageContext.request.contextPath}/tasks/' + taskId, { method: 'DELETE' })
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