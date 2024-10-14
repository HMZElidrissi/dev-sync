<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:layout title="Manager Overview">
    <div class="container mx-auto px-4">
        <h1 class="text-2xl font-bold mb-4">Team Overview</h1>

        <c:forEach var="entry" items="${overview}">
            <div class="bg-white shadow-md rounded-lg p-4 mb-4">
                <h2 class="text-xl font-semibold mb-2">${entry.key}</h2>
                <p>Number of Tasks: ${entry.value.numberOfTasks}</p>
                <p>Tasks Completed this Week: ${entry.value.tasksCompletedThisWeek}</p>
                <p>Tokens Used: ${entry.value.tokensUsed}</p>
            </div>
        </c:forEach>
    </div>
</t:layout>