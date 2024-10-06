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
<body class="bg-gray-100 font-sans">
<div class="flex h-screen">
    <!-- Sidebar -->
    <div class="w-64 bg-gray-800 text-white">
        <div class="p-6">
            <img src="${pageContext.request.contextPath}/resources/images/dev-sync.png" alt="DevSync Logo"
                 class="h-14 mx-auto">
        </div>
        <nav class="mt-8">
            <a href="${pageContext.request.contextPath}/users"
               class="flex items-center py-3 px-6 text-sm hover:bg-gray-400 transition duration-200">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24"
                     stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path>
                </svg>
                Users Management
            </a>
        </nav>
    </div>

    <!-- Main content -->
    <div class="flex-1 flex flex-col overflow-hidden">
        <!-- Header -->
        <header class="bg-white shadow-sm">
            <div class="max-w-7xl mx-auto py-4 px-4 sm:px-6 lg:px-8 flex justify-between items-center">
                <h1 class="text-2xl font-semibold text-gray-900">${title}</h1>
                <div class="flex items-center">
                    <span class="text-sm text-gray-500 mr-4">Welcome, User</span>
                    <button class="bg-gray-700 hover:bg-gray-900 text-white px-4 py-2 rounded-md text-sm transition duration-200">
                        Logout
                    </button>
                </div>
            </div>
        </header>

        <!-- Page content -->
        <main class="flex-1 overflow-x-hidden overflow-y-auto bg-gray-100">
            <div class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
                <div class="bg-white shadow-md rounded-lg p-6">
                    <jsp:doBody/>
                </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>