<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="User List">
    <div class="bg-white shadow-md rounded-lg overflow-hidden">
        <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
            <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Username</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Email</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Role</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
            </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
            <c:forEach var="user" items="${users}">
                <tr>
                    <td class="px-6 py-4 whitespace-nowrap">${user.id}</td>
                    <td class="px-6 py-4 whitespace-nowrap">${user.username}</td>
                    <td class="px-6 py-4 whitespace-nowrap">${user.firstName} ${user.lastName}</td>
                    <td class="px-6 py-4 whitespace-nowrap">${user.email}</td>
                    <td class="px-6 py-4 whitespace-nowrap">${user.role}</td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <a href="${pageContext.request.contextPath}/users/${user.id}" class="text-indigo-600 hover:text-indigo-900 mr-2">View</a>
                        <a href="${pageContext.request.contextPath}/users/edit/${user.id}" class="text-blue-600 hover:text-blue-900 mr-2">Edit</a>
                        <a href="${pageContext.request.contextPath}/users/delete/${user.id}" class="text-red-600 hover:text-red-900" onclick="return confirm('Are you sure you want to delete this user?')">Delete</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="mt-6">
        <a href="${pageContext.request.contextPath}/users/new" class="inline-block bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
            Create New User
        </a>
    </div>
</t:layout>