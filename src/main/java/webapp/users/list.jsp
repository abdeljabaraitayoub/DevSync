<%@ page import="java.util.List" %>
<%@ page import="com.devsync.domain.entities.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdn.jsdelivr.net/npm/flowbite@2.5.2/dist/flowbite.min.css"  rel="stylesheet" />
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>





<div class="relative  mx-auto mt-18  p-20  ">
    <a class="bg-blue-500 hover:bg-blue-700   btn rigth mx-6 text-white font-bold py-2 px-4 rounded " href="/dev/users?action=create"> Create </a>

    <table class="w-1/2 mx-auto text-sm text-left  mt-5 rtl:text-right text-gray-500 ">
        <thead class="text-xs text-gray-700 uppercase bg-gray-50 ">
        <tr>
            <th scope="col" class="px-6 py-3">
                ID
            </th>
            <th scope="col" class="px-6 py-3">
                userName
            </th>
            <th scope="col" class="px-6 py-3">
                name
            </th>
            <th scope="col" class="px-6 py-3">
                prenom
            </th>
            <th scope="col" class="px-6 py-3">
                email
            </th>
            <th scope="col" class="px-6 py-3">
                User type
            </th>
            <th scope="col" class="px-6 py-3">
               Actions
            </th>
        </tr>
        </thead>
        <tbody>
        <%
            List<User> users = (List<User>) request.getAttribute("users");
            if (users != null) {
                for (User user : users) {
        %>
        <tr class="bg-white border-b">
            <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap ">
                <%= user.getId() %>
            </th>
            <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap ">
                <%= user.getUsername() %>
            </th>
            <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap ">
                <%= user.getName() %>
            </th>
            <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
                <%= user.getPrenom() %>
            </th>
            <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap ">
                <%= user.getEmail() %>
            </th>
            <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap ">
                <%= user.getUserType() %>
            </th>
            <td class="px-6 py-4 flex">
                <form action="/dev/users" method="post">
                    <input type="hidden" name="id" value="<%= user.getId() %>">
                    <input type="hidden" name="_method" value="DELETE">
                    <button type="submit" class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded delete-btn">Delete</button>
                </form>
                <form action="/dev/users" method="post">
                    <input type="hidden" name="id" value="<%= user.getId() %>">
                    <input type="hidden" name="_method" value="edit">
                    <button type="submit" class="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">Update</button>
                </form>
            </td>
        </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>


</div>

<script>
    const deleteButtons = document.querySelectorAll('.delete-btn');
    deleteButtons.forEach((button) => {
        button.addEventListener('click', (event) => {
            event.preventDefault();
            const form = button.parentElement;
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire({
                        title: "Deleted!",
                        text: "User has been deleted.",
                        icon: "success"
                    });
                    setTimeout(() => {
                        form.submit();
                    }, 1000);

                }
            });
        });
    });
</script>
<script src="https://cdn.jsdelivr.net/npm/flowbite@2.5.2/dist/flowbite.min.js"></script>
</body>
</html>