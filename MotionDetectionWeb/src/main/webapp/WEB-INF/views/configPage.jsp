<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<spring:url value="/css/imageRecognition.css" var="imageRecognitionCss"/>
<spring:url value="/js/jquery-3.1.0.min.js" var="jqueryJs"/>

<html lang="en">

<head>
  <meta charset="utf-8">
  <link href="${imageRecognitionCss}" rel="stylesheet"/>
  <script src="${jqueryJs}" type="application/javascript"></script>
</head>

<body>
<div class="container">

  <div class="starter-template jumbotron">
    <h1>Device configuration</h1>
    <form>
      <div>
        <c:if test="${not empty sensitivity}">
          <label for="sensitivity">Sensitivity</label>
          <select id="sensitivity" required>
            <c:forEach var="sensitivityOption" items="${sensitivity}">
              <option value="${sensitivityOption}">${sensitivityOption}</option>
            </c:forEach>
          </select>
        </c:if>
      </div>
      <div>
        <label for="minimumMotionFrames"></label>
        <input type="text" id="minimumMotionFrames" required />
      </div>
      <div>
        <label for="resolutionWidth"></label>
        <input type="text" id="resolutionWidth" required />
      </div>
      <div>
        <label for="resolutionHeight"></label>
        <input type="text" id="resolutionHeight" required />
      </div>
    </form>
  </div>

</div>

</body>
</html>