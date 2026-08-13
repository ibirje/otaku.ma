<!doctype html>
<html>
<head>
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <title>404 Theme Inexistante</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css"/>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css" type="text/css" media="all"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/animate.min.css" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/flexslider.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css" type="text/css"/>
    
    <script src="${pageContext.request.contextPath}/js/wow.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/fonctions.js"></script>
    
	<script>
     wow = new WOW({
		 live:true
		 })
	 wow.init();
    </script>

</head>

<!-- TODO CAROUSEL SLIDESHOW -->

<body style="background-color:#CEECFF">

<jsp:include page="header.jsp"></jsp:include>
	<div id="container">
		<div id="main" class="col-lg-12 col-md-12 col-sm-12 col-xs-12" >
			<div  class="col-lg-12 col-md-12 col-sm-12 col-xs-12 bordered" style="padding-top:50px;padding-bottom:50px;margin-top:200px;margin-bottom:200px;text-align: center">
				Theme Inexistante
			</div>
		</div>
	</div>
<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>
