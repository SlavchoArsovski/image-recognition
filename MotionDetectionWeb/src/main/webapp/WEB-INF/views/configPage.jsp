<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<spring:url value="/css/bootstrap.min.css" var="bootstrapCss"/>
<spring:url value="/css/config.css" var="configCss"/>
<spring:url value="/js/jquery-3.1.0.min.js" var="jqueryJs"/>
<spring:url value="/js/bootstrap.min.js" var="bootstrapJs"/>
<spring:url value="/assets/favicon.png" var="favicon"/>

<html lang="en">

<head>
  <meta charset="utf-8">
  <title>Motion Detection - Config</title>
  <link rel="icon" href="${favicon}" type="image/png">
  <link rel="apple-touch-icon" href="${favicon}">
  <link href="${bootstrapCss}" rel="stylesheet"/>
  <link href="${configCss}" rel="stylesheet"/>
  <script src="${jqueryJs}" type="application/javascript"></script>
  <script src="${bootstrapJs}" type="application/javascript"></script>
  <script type="text/javascript">
    function setFormAction(formId) {
      var action = '/config';
      var selectedDeviceId = document.getElementById('deviceIdSelect').value;
      if (selectedDeviceId) {
        document.getElementById(formId).action = action + '?deviceId=' + selectedDeviceId;
      } else {
        document.getElementById(formId).action = action;
      }
      document.getElementsByName('deviceId')[0].value=selectedDeviceId;
      document.getElementsByName('deviceId')[1].value=selectedDeviceId;
    }
    function submitFormWithId(formId) {
      setFormAction(formId);
      document.getElementById(formId).submit();
    }
  </script>
</head>
<body>
<div class="container">
  <div class="starter-template jumbotron">
    <form method="GET" action="/config" name="updateDeviceId" id="updateDeviceId">
      <div class="header">
        <div class="headerTitle">Device configuration</div>
        <div class="deviceOptions">
          <c:if test="${not empty devices}">
            <select
                    id="deviceIdSelect"
                    class="form-control"
                    title="Select the device id"
                    onchange="javascript:submitFormWithId('updateDeviceId')"
                    required>
              <option value="" disabled selected>Device ID</option>
              <c:forEach var="deviceIdOption" items="${devices}">
                <option value="${deviceIdOption}" ${deviceIdOption == monitoringConfig.deviceId ? 'selected' : ''}>
                    ${deviceIdOption}
                </option>
              </c:forEach>
            </select>
          </c:if>
        </div>
      </div>
      <input type="hidden" name="deviceId" />
    </form>
    <form:form method="POST" modelAttribute="monitoringConfig" action="/config">
      <div class="cTable table configTableContainer">
        <div class="cTableRow">
          <div class="cTableCell tableCell">
            <div class="cTable table">
              <div class="cTableHeading">
                <h3>Functional configuration</h3>
              </div>
              <div class="cTableRow">
                <div class="cTableCell">
                  <c:if test="${not empty sensitivityOptions}">
                    <label for="sensitivity">
                      Sensitivity
                    </label>
                    <div>
                      <form:select
                          path="sensitivity"
                          id="sensitivity"
                          name="sensitivity"
                          class="form-control"
                          title="Defines the sensitivity (The higher the value, the lesser the sensitivity)"
                          required="true"
                      >
                        <c:forEach var="sensitivityOption" items="${sensitivityOptions}">
                          <option ${sensitivityOption == monitoringConfig.sensitivity ? 'selected' : ''} value="${sensitivityOption}">
                              ${sensitivityOption}
                          </option>
                        </c:forEach>
                      </form:select>
                    </div>
                  </c:if>
                </div>
              </div>
              <div class="cTableRow">
                <div class="cTableCell">
                  <c:if test="${not empty resolutionOptions}">
                    <label for="resolution">
                      Resolution
                    </label>
                    <div>
                      <select
                          id="resolution"
                          name="resolution"
                          class="form-control"
                          title="Image resolution"
                          required="true">
                        <c:forEach var="resolutionOption" items="${resolutionOptions}">
                          <option ${resolutionOption == monitoringConfig.resolution ? 'selected' : ''} value="${resolutionOption}">
                              ${resolutionOption}
                          </option>
                        </c:forEach>
                      </select>
                    </div>
                  </c:if>
                </div>
              </div>
              <div class="cTableRow">
                <div class="cTableCell">
                  <c:if test="${not empty minimumMotionFramesOptions}">
                    <label for="minimumMotionFrames">
                      Minimum motion frames
                    </label>
                    <div>
                      <select
                          id="minimumMotionFrames"
                          name="minimumMotionFrames"
                          class="form-control"
                          title="Defines the minimum number of frames with difference in motion that will trigger motion"
                          required="true">
                        <c:forEach var="minimumMotionFramesOption" items="${minimumMotionFramesOptions}">
                          <option
                            ${minimumMotionFramesOption == monitoringConfig.minimumMotionFrames ? 'selected' : ''}
                              value="${minimumMotionFramesOption}">
                              ${minimumMotionFramesOption}
                          </option>
                        </c:forEach>
                      </select>
                    </div>
                  </c:if>
                </div>
              </div>
            </div>
          </div>
          <div class="cTableCell">
            <div class="cTable table">
              <div class="cTableHeading">
                <h3>Style configuration</h3>
              </div>
              <div class="cTableRow">
                <div class="cTableCell">
                  <label id="statusFontColorLabel" for="statusFontColor">
                    Status font color
                  </label>
                  <div>
                    <form:input
                        path="statusFontColor"
                        id="statusFontColor"
                        name="statusFontColor"
                        type="color"
                        class="form-control"
                        title="Font color of the text that displays the status"
                        onchange="javascript:document.getElementById('statusFontColorLabel').style.color = document.getElementById('statusFontColor').value;"
                    />
                  </div>
                </div>
              </div>
              <div class="cTableRow">
                <div class="cTableCell">
                  <label id="timestampFontColorLabel" for="timestampFontColor">
                    Timestamp font color
                  </label>
                  <div>
                    <form:input
                        path="timestampFontColor"
                        id="timestampFontColor"
                        name="timestampFontColor"
                        type="color"
                        class="form-control"
                        title="Font color of the text that displays the timestamp"
                        onchange="javascript:document.getElementById('timestampFontColorLabel').style.color = document.getElementById('timestampFontColor').value;"
                    />
                  </div>
                </div>
              </div>
              <div class="cTableRow">
                <div class="cTableCell">
                  <label id="motionIndicatorColorLabel" for="motionIndicatorColor">
                    Motion detected frame color
                  </label>
                  <div>
                    <form:input
                        path="motionIndicatorColor"
                        id="motionIndicatorColor"
                        name="motionIndicatorColor"
                        type="color"
                        class="form-control"
                        onchange="javascript:document.getElementById('motionIndicatorColorLabel').style.color = document.getElementById('motionIndicatorColor').value;"
                        title="Color of the frame that indicates movement"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="cTableRow">
          <div class="cTableCell">
          </div>
          <div class="cTableCell">
            <button type="submit" class="btn-info submit-button">
              Save Configuration
            </button>
          </div>
        </div>
      </div>
      <form:input type="hidden" path="deviceId" name="deviceId" id="deviceId"/>
    </form:form>
  </div>
</div>
</body>
</html>