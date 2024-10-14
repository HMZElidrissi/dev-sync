<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Unauthorized Access - DevSync</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100 flex items-center justify-center min-h-screen">
<div class="bg-white p-8 rounded-lg shadow-md w-96 text-center">
    <h2 class="text-2xl font-semibold text-red-500 mb-4">Unauthorized Access</h2>
    <p class="text-gray-700 mb-6">You do not have permission to access this page.</p>
    <a href="${pageContext.request.contextPath}/login" class="bg-gray-700 hover:bg-gray-900 text-white py-2 px-4 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50">
        Return to Login
    </a>
</div>
</body>
</html>