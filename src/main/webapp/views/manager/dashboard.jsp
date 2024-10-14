<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="Manager Dashboard">
    <div class="bg-white shadow-md rounded-lg p-6">
        <h2 class="text-2xl font-bold mb-4">Welcome, ${sessionScope.user.firstName}</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
                <h3 class="text-xl font-semibold mb-2">Quick Actions</h3>
                <ul class="space-y-2">
                    <li><a href="${pageContext.request.contextPath}/admin/tasks/new" class="text-blue-600 hover:underline">Create New Task</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/users" class="text-blue-600 hover:underline">Manage Users</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/tasks" class="text-blue-600 hover:underline">View All Tasks</a></li>
                </ul>
            </div>
            <div>
                <h3 class="text-xl font-semibold mb-2">Recent Activities</h3>
                <!-- Add recent activities here -->
            </div>
        </div>
    </div>
</t:layout>