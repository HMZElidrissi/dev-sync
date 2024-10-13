<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="View User">
    <div class="bg-white shadow-md rounded-lg overflow-hidden">
        <table class="min-w-full divide-y divide-gray-200">
            <tbody class="bg-white divide-y divide-gray-200">
            <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                <td class="px-6 py-4 whitespace-nowrap">${user.id}</td>
            </tr>
            <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Username</th>
                <td class="px-6 py-4 whitespace-nowrap">${user.username}</td>
            </tr>
            <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">First Name</th>
                <td class="px-6 py-4 whitespace-nowrap">${user.firstName}</td>
            </tr>
            <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Last Name</th>
                <td class="px-6 py-4 whitespace-nowrap">${user.lastName}</td>
            </tr>
            <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Email</th>
                <td class="px-6 py-4 whitespace-nowrap">${user.email}</td>
            </tr>
            <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Role</th>
                <td class="px-6 py-4 whitespace-nowrap">${user.role}</td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="mt-6">
        <a href="${pageContext.request.contextPath}/admin/users" class="inline-block bg-gray-700 hover:bg-gray-900 text-white font-bold py-2 px-4 rounded mr-2">
            Back to User List
        </a>
        <a href="${pageContext.request.contextPath}/users/edit/${user.id}" class="inline-block bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
            Edit User
        </a>
    </div>
</t:layout>