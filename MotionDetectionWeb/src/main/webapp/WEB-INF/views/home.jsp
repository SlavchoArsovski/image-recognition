<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<spring:url value="/css/bootstrap.min.css" var="bootstrapCss"/>
<spring:url value="/css/home.css" var="homeCss"/>
<spring:url value="/css/jquery-ui.min.css" var="jqueryUiCss"/>
<spring:url value="/js/jquery-3.1.0.min.js" var="jqueryJs"/>
<spring:url value="/js/jquery-ui.min.js" var="jqueryUiJs"/>
<spring:url value="/js/bootstrap.min.js" var="bootstrapJs"/>
<spring:url value="/js/sliderComponent.js" var="sliderComponentJs"/>
<spring:url value="/js/home.js" var="homeJs"/>
<spring:url value="/assets/favicon.png" var="favicon"/>

<html>

<head>
  <meta charset="utf-8">
  <title>Motion Detection - Home</title>
  <link rel="icon" href="${favicon}" type="image/png">
  <link href="${jqueryUiCss}" rel="stylesheet"/>
  <link href="${bootstrapCss}" rel="stylesheet"/>
  <link href="${homeCss}" rel="stylesheet"/>
  <script src="${jqueryJs}" type="application/javascript"></script>
  <script src="${bootstrapJs}" type="application/javascript"></script>
  <script src="${jqueryUiJs}" type="application/javascript"></script>
  <script src="${sliderComponentJs}" type="application/javascript"></script>
  <script src="${homeJs}" type="application/javascript"></script>
  <title>Motion Detection</title>

  <script>
    motionDetectionApp = {
      conf: {
        contextPath: '${SERVLET_CONTEXT_PATH}'
      }
    }
  </script>

</head>

<body>
<h1>
  Motion Detection
  <a class="configLink" href="config" target="_blank">Config</a>
</h1>
<div class="content">
  <div class="selectionContainer">

    <p>Selected client: </p>
    <div>
      <select id="selectClientDropDown">
        <c:forEach items="${clients}" var="client">
          <option value="${client}">
              ${client}
          </option>
        </c:forEach>
      </select>
    </div>

    <p>Selected date: <label id="selectedDate"></label></p>
    <div id="datepicker">
    </div>

    <div>
      <p>Selected time range: <label id="selectedTimeRange"></label></p>
      <div class="slider-wrapper">
        <div id="slider-range"></div>
      </div>
    </div>
  </div>

  <div class="image-view-wrapper">
    <div class="image-view-container"></div>
  </div>

</div>

<div class="product-image-overlay">
  <img src=""/>
  </div>

</body>
</html>