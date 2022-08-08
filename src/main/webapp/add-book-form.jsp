<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>ADD NEW BOOK</title>
</head>
<body>
<h1>ADD NEW BOOK</h1>
<br/>


<form action="/add-book" method="post" enctype="multipart/form-data">

    <label for="title">Title:</label>

    <input id="title" type="text" name="title">

    <br>
    <label for="description">Description:</label>

    <textarea id="description" name="description"></textarea>

    <br>
    <label for="authorsIds">Title:</label>
    <select id="authorsIds" multiple name="authorsIds">
        <option disabled value="0">Select authors:</option>
        <c:forEach items="${authorList}" var="author">
            <option value="${author.getId()}">${author.getFullName()}</option>
        </c:forEach>
    </select>

    <br>
    <label for="categoryId">Select category:</label>

    <select id="categoryId" name="categoryId">
        <option disabled value="0">Select category:</option>
        <c:forEach items="${categoryList}" var="category">
            <option value="${category.getId()}">${category.getName()}</option>
        </c:forEach>
    </select>

    <br>
    <label for="isbn">Isbn:</label>

    <input id="isbn" type="text" name="isbn">

    <br>
    <label for="year">Year:</label>

    <input id="year" type="number" name="year">

    <br>
    <label for="image">Upload cover image:</label>

    <input  id="image" type="file" name="image">

    <br>

    <input type="submit" value="Saqlash">

</form>
</body>
</html>