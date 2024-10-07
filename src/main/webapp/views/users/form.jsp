<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="${empty user ? 'Create' : 'Edit'} User">
    <div class="bg-white shadow-md rounded-lg p-6">
        <form action="${pageContext.request.contextPath}/users" method="POST">
            <input type="hidden" name="action" value="${empty user ? 'create' : 'update'}">
            <c:if test="${not empty user}">
                <input type="hidden" name="id" value="${user.id}">
            </c:if>

            <div class="mb-4">
                <label for="username" class="block text-gray-700 text-sm font-bold mb-2">Username:</label>
                <input type="text" id="username" name="username" value="${user.username}" required
                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>

            <div class="mb-4">
                <label for="password" class="block text-gray-700 text-sm font-bold mb-2">Password:</label>
                <input type="password" id="password" name="password" ${empty user ? 'required' : ''}
                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>

            <div class="mb-4">
                <label for="firstName" class="block text-gray-700 text-sm font-bold mb-2">First Name:</label>
                <input type="text" id="firstName" name="firstName" value="${user.firstName}"
                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>

            <div class="mb-4">
                <label for="lastName" class="block text-gray-700 text-sm font-bold mb-2">Last Name:</label>
                <input type="text" id="lastName" name="lastName" value="${user.lastName}"
                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>

            <div class="mb-4">
                <label for="email" class="block text-gray-700 text-sm font-bold mb-2">Email:</label>
                <input type="email" id="email" name="email" value="${user.email}" required
                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>

            <div class="mb-6">
                <label for="role" class="block text-gray-700 text-sm font-bold mb-2">Role:</label>
                <select id="role" name="role" required
                        class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    <option value="USER" ${user.role == 'USER' ? 'selected' : ''}>User</option>
                    <option value="MANAGER" ${user.role == 'MANAGER' ? 'selected' : ''}>Manager</option>
                </select>
            </div>

            <div class="flex items-center justify-between">
                <button type="submit" class="bg-gray-700 hover:bg-gray-900 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
                        ${empty user ? 'Create' : 'Update'} User
                </button>
                <a href="${pageContext.request.contextPath}/users" class="inline-block align-baseline font-bold text-sm text-gray-700 hover:text-gray-900 hover:underline">
                    Cancel
                </a>
            </div>
        </form>
    </div>
</t:layout>