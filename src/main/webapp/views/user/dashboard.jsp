<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="User Dashboard">
    <div class="bg-white shadow-md rounded-lg p-6">
        <h2 class="text-2xl font-bold mb-4">Welcome, ${sessionScope.user.firstName}</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
                <h3 class="text-xl font-semibold mb-2">Your Tasks</h3>
                <ul class="space-y-2">
                    <li><a href="${pageContext.request.contextPath}/user/tasks?filter=assigned" class="text-blue-600 hover:underline">View Assigned Tasks</a></li>
                    <li><a href="${pageContext.request.contextPath}/user/tasks/new" class="text-blue-600 hover:underline">Create Personal Task</a></li>
                </ul>
            </div>
            <div>
                <h3 class="text-xl font-semibold mb-2">Token Information</h3>
                <p>Daily Replace Tokens: ${sessionScope.user.token.dailyReplaceTokens}</p>
                <p>Monthly Delete Tokens: ${sessionScope.user.token.monthlyDeleteTokens}</p>
            </div>
        </div>
    </div>
</t:layout>