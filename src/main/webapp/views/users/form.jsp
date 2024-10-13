<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="${empty editUser.id ? 'Create' : 'Edit'} User">
    <div class="bg-white shadow-md rounded-lg p-6">
        <form action="${pageContext.request.contextPath}/admin/users" method="POST">
            <input type="hidden" name="action" value="${empty editUser.id ? 'create' : 'update'}">
            <c:if test="${not empty editUser.id}">
                <input type="hidden" name="id" value="${editUser.id}">
            </c:if>

            <div class="mb-4">
                <label for="username" class="block text-gray-700 text-sm font-bold mb-2">Username:</label>
                <input type="text" id="username" name="username" value="${editUser.username}" required
                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>

            <div class="mb-4">
                <label for="password" class="block text-gray-700 text-sm font-bold mb-2">Password:</label>
                <input type="password" id="password" name="password" ${empty editUser.id ? 'required' : ''}
                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                <c:if test="${not empty editUser.id}">
                    <small class="text-gray-500">Leave blank to keep current password</small>
                </c:if>
            </div>

            <div class="mb-4">
                <label for="firstName" class="block text-gray-700 text-sm font-bold mb-2">First Name:</label>
                <input type="text" id="firstName" name="firstName" value="${editUser.firstName}" required
                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>

            <div class="mb-4">
                <label for="lastName" class="block text-gray-700 text-sm font-bold mb-2">Last Name:</label>
                <input type="text" id="lastName" name="lastName" value="${editUser.lastName}" required
                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>

            <div class="mb-4">
                <label for="email" class="block text-gray-700 text-sm font-bold mb-2">Email:</label>
                <input type="email" id="email" name="email" value="${editUser.email}" required
                       class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>

            <div class="mb-6">
                <label for="role" class="block text-gray-700 text-sm font-bold mb-2">Role:</label>
                <select id="role" name="role" required
                        class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    <option value="USER" ${editUser.role == 'USER' ? 'selected' : ''}>User</option>
                    <option value="MANAGER" ${editUser.role == 'MANAGER' ? 'selected' : ''}>Manager</option>
                </select>
            </div>

            <div class="flex items-center justify-between">
                <button type="submit" class="bg-gray-700 hover:bg-gray-900 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
                        ${empty editUser.id ? 'Create' : 'Update'} User
                </button>
                <a href="${pageContext.request.contextPath}/admin/users" class="inline-block align-baseline font-bold text-sm text-gray-700 hover:text-gray-900 hover:underline">
                    Cancel
                </a>
            </div>
        </form>
    </div>
</t:layout>