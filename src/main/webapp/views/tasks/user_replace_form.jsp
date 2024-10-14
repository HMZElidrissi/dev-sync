<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="Replace Task">
    <div class="bg-white shadow-md rounded-lg p-6">
        <h2 class="text-2xl font-bold mb-4">Replace Task: ${task.title}</h2>
        <form action="${pageContext.request.contextPath}/user/tasks/replace/${task.id}" method="POST">
            <div class="mb-4">
                <label for="title" class="block text-gray-700 text-sm font-bold mb-2">New Title:</label>
                <input type="text" id="title" name="title" required
                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>

            <div class="mb-4">
                <label for="description" class="block text-gray-700 text-sm font-bold mb-2">New Description:</label>
                <textarea id="description" name="description" rows="3" required
                          class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"></textarea>
            </div>

            <div class="mb-4">
                <label for="dueDate" class="block text-gray-700 text-sm font-bold mb-2">New Due Date:</label>
                <input type="date" id="dueDate" name="dueDate" required
                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>

            <div class="mb-4">
                <label for="tags" class="block text-gray-700 text-sm font-bold mb-2">New Tags (comma-separated):</label>
                <input type="text" id="tags" name="tags" required
                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>

            <div class="flex items-center justify-between">
                <button type="submit" class="bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
                    Replace Task
                </button>
                <a href="${pageContext.request.contextPath}/user/tasks" class="inline-block align-baseline font-bold text-sm text-blue-500 hover:text-blue-800">
                    Cancel
                </a>
            </div>
        </form>
    </div>
</t:layout>