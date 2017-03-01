<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<spring:url value="/css/imageRecognition.css" var="imageRecognitionCss"/>
<spring:url value="/css/bootstrap.css" var="bootstrapCss"/>
<spring:url value="/js/jquery-3.1.0.min.js" var="jqueryJs"/>
<spring:url value="/js/dist/bootstrap.js" var="bootstrapJs"/>
<spring:url value="/js/home.js" var="homeJs"/>
<spring:url value="/js/CameraControllerMock.js" var="CameraControllerMock"/>

<html>

<head>
  <link href="${bootstrapCss}" rel="stylesheet">
  <link href="${imageRecognitionCss}" rel="stylesheet"/>
  <script src="${jqueryJs}" type="application/javascript"></script>
  <script src="${bootstrapJs}" type="application/javascript"></script>
  <script src="${CameraControllerMock}" type="application/javascript"></script>
</head>

<body>
<div class="container">

  <div class="starter-template jumbotron">
    <h1>Motion Detection - Mock Web Client</h1>
    <input id="f" type="file" value="Select an image"/>
    <div>
      <input type="button" name="getConfig" id="getConfig" value="Get config" />
      <input type="button" name="sendContinuous" id="sendContinuous" value="Send stream" />
      <input type="button" name="sendRed" id="sendRed" value="Send red" />
      <input type="button" name="sendBlue" id="sendBlue" value="Send blue" />
      <input type="button" name="sendGreen" id="sendGreen" value="Send green" />
      <input type="button" name="sendYellow" id="sendYellow" value="Send yellow" />
    </div>
  </div>

</div>

</body>
</html>