<%@ tag description="Dashboard Layout" pageEncoding="UTF-8" %>
<%@ attribute name="title" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title} - DevSync</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100">
<div class="flex h-screen">
    <!-- Sidebar -->
    <div class="w-64 bg-gray-800 text-white">
        <div class="p-4">
            <img src="${pageContext.request.contextPath}/images/devsync-logo.png" alt="DevSync Logo" class="h-8">
        </div>
        <nav class="mt-6">
            <a href="${pageContext.request.contextPath}/users" class="block py-2 px-4 text-sm hover:bg-gray-700">Users</a>
            <!-- Add more sidebar items here as needed -->
        </nav>
    </div>

    <!-- Main content -->
    <div class="flex-1 flex flex-col overflow-hidden">
        <!-- Header -->
        <header class="bg-white shadow">
            <div class="max-w-7xl mx-auto py-4 px-4 sm:px-6 lg:px-8">
                <h1 class="text-2xl font-bold text-gray-900">${title}</h1>
            </div>
        </header>

        <!-- Page content -->
        <main class="flex-1 overflow-x-hidden overflow-y-auto bg-gray-100">
            <div class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
                <jsp:doBody/>
            </div>
        </main>
    </div>
</div>
</body>
</html>