<%--
  Created by IntelliJ IDEA.
  User: NIZAMUDDIN ALI AHMED
  Date: 09-Aug-20
  Time: 10:39 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>StockRock</title>
    <link rel="icon" href="${pageContext.request.contextPath}/resources/assets/img/favicon.png" type="image/x-icon">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/libs/css/jquery.terminal.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/assets/css/terminal.css"/>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/libs/css/goldenlayout-base.css"/>
    <%--    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/libs/css/goldenlayout-light-theme.css"/>--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/libs/css/goldenlayout-dark-theme.css"/>

    <script src="${pageContext.request.contextPath}/resources/libs/js/jquery-3.3.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/libs/js/jquery.terminal.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/libs/js/autocomplete_menu.js"></script>
    <script src="${pageContext.request.contextPath}/resources/libs/js/figlet.js"></script>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/assets/css/chart.css">
    <script src="${pageContext.request.contextPath}/resources/libs/js/d3.v4.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/libs/js/techan.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/libs/js/plotly-latest.min.js"></script>

    <script src="${pageContext.request.contextPath}/resources/libs/js/goldenlayout.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/libs/js/goldenlayout.js"></script>

    <script src="${pageContext.request.contextPath}/resources/assets/js/graphs/graph.config.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/graphs/graph.constants.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/graphs/graph.objects.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/graphs/graph.render.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/graphs/graph.controller.js"></script>

    <script src="${pageContext.request.contextPath}/resources/assets/js/fun/server.terminal.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/fun/youtube.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/fun/google.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/connection/socket.config.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/connection/socket.controller.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/collectors/collector.model.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/collectors/chart.data.collector.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/global.controller.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/global.styles.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/global.constants.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/variables/global.variables.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/constants/terminal.constants.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/constants/chart.constants.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/variables/term.variables.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/variables/chart.variables.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/utils.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/functions/chart.functions.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/functions/help.functions.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/functions/server.terminal.functions.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/functions/movie.functions.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/functions/dictionary.functions.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/functions/youtube.functions.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/functions/google.functions.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/global.functions.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/chart.config.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/chart.controller.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/app.html.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/app.config.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/app.js"></script>
</head>
<body onload="startTime()" >

<div id="header" style="width: 100%; height: 4vh; color: white">
    <div id="symbols-head" style="font-size: large; margin: 5px">
        <span style="font-size: large;">
            DATA METRICS
        </span>
        <span id="spinner" style="color: white">
            ▰▰▰▰▰▰▰▰▰▰
        </span>

        <span id="time" style="color: white; margin-top: 5px; float: right"></span>

        <span id="live-icon" style="display: none; float: left; margin-top: 5px; margin-right: 10px" ><img style="background: white; width: 40px" src="${pageContext.request.contextPath}/resources/assets/img/live.gif"></span>

    </div>
</div>
<div id="container" style="width: 100%; height: 95vh"></div>

</body>
</html>

<script>

    function startTime() {
        var today = new Date();
        var h = today.getHours();
        var m = today.getMinutes();
        var s = today.getSeconds();
        m = checkTime(m);
        s = checkTime(s);
        document.getElementById('time').innerHTML =
            h + ":" + m + ":" + s;
        var t = setTimeout(startTime, 500);
    }
    function checkTime(i) {
        if (i < 10) {i = "0" + i}
        return i;
    }

</script>
