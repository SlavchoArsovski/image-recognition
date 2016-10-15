<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<spring:url value="/css/imageRecognition.css" var="imageRecognitionCss"/>
<spring:url value="/js/jquery-3.1.0.min.js" var="jqueryJs"/>
<spring:url value="/js/home.js" var="homeJs"/>

<html>

<head>
    <link href="${imageRecognitionCss}" rel="stylesheet" />
    <script src="${jqueryJs}" type="application/javascript"></script>
    <script src="${homeJs}" type="application/javascript"></script>
</head>

<body>
    <h1>Image Recognition</h1>

    <img id="imageRecognition" class="image-recognition"
         src=""
         alt="image no found"/>
</body>
</html>