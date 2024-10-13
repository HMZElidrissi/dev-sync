<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="${empty task ? 'Create' : 'Edit'} Task">
  <div class="bg-white shadow-md rounded-lg p-6">

    <c:if test="${not empty error}">
      <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
        <span class="block sm:inline">${error}</span>
      </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/admin/tasks" method="POST">
      <input type="hidden" name="action" value="${empty task ? 'create' : 'update'}">
      <c:if test="${not empty task}">
        <input type="hidden" name="id" value="${task.id}">
      </c:if>

      <div class="mb-4">
        <label for="title" class="block text-gray-700 text-sm font-bold mb-2">Title:</label>
        <input type="text" id="title" name="title" value="${task.title}" required
               class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
      </div>

      <div class="mb-4">
        <label for="description" class="block text-gray-700 text-sm font-bold mb-2">Description:</label>
        <textarea id="description" name="description" rows="3" required
                  class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">${task.description}</textarea>
      </div>

      <div class="mb-4">
        <label for="dueDate" class="block text-gray-700 text-sm font-bold mb-2">Due Date:</label>
        <input type="date" id="dueDate" name="dueDate" value="${task.dueDate}" required
               class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
      </div>

      <div class="mb-4">
        <label for="tags" class="block text-gray-700 text-sm font-bold mb-2">Tags (comma-separated):</label>
        <input type="text" id="tags" name="tags" value="${task.tags}" required
               class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
      </div>

      <div class="mb-4">
        <label for="assignedTo" class="block text-gray-700 text-sm font-bold mb-2">Assign To:</label>
        <select id="assignedTo" name="assignedTo" required
                class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
          <c:forEach var="user" items="${users}">
            <option value="${user.id}" ${task.assignedTo.id == user.id ? 'selected' : ''}>${user.username}</option>
          </c:forEach>
        </select>
      </div>

      <div class="flex items-center justify-between">
        <button type="submit" class="bg-gray-700 hover:bg-gray-900 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
            ${empty task ? 'Create' : 'Update'} Task
        </button>
        <a href="${pageContext.request.contextPath}/admin/tasks" class="inline-block align-baseline font-bold text-sm text-blue-500 hover:text-blue-800">
          Cancel
        </a>
      </div>
    </form>
  </div>
</t:layout>