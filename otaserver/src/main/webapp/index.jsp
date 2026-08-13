<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="/error_produit_not_found.jsp" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <jsp:useBean id="random" class="java.util.Random" scope="application" />
<html>
	<head>
	   
	    <title>Plein de goodies et accessoires de jeux video et de mangas sur Otaku.ma </title>
	    
	    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css"/>
	    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/indexcomingsoon.css" type="text/css"/>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
	    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/animate.min.css" type="text/css"/>
	    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/produit.css" type="text/css" /> 
	    
	    <script src="${pageContext.request.contextPath}/js/wow.min.js"></script>
			  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
			  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	    <script src="${pageContext.request.contextPath}/js/fonctions.js"></script>
	    
		<script>
	     wow = new WOW({
			 live:true
			 })
		 wow.init();
	    </script>
	
	</head>

<!-- TODO CAROUSEL SLIDESHOW IN INDEX -->

<body style="background-color:#CEECFF; height:100%;overflow:hidden;">

		
		
	<div class="fadeOut wow" data-wow-duration="1" data-wow-delay="10s" style="width: 100%">
	
	<c:forEach begin="0" end="60" varStatus="boucle">
	
		<div  class="zawa wow" data-wow-duration="${1+ random.nextFloat()*2 }s" data-wow-delay="${ boucle.index/8 + random.nextFloat() }s" style="visibility:hidden;position: absolute; right:${1+random.nextInt(105)}%; top :${1+random.nextInt(105)}%;">
			<img class="row" src ="${pageContext.request.contextPath}/img/zawa.png" style=" width: ${30+ random.nextFloat()*20 }%"/>
		</div>
		
		<div  class="zawa wow" data-wow-duration="${1+ random.nextFloat()*2 }s" data-wow-delay="${ 0.1 + boucle.index/8 + random.nextFloat() }s" style="visibility:hidden;position: absolute; left:${1+random.nextInt(105)}%; bottom :${1+random.nextInt(105)}%;">
			<img class="row" src ="${pageContext.request.contextPath}/img/zawa.png" style="width: ${30+ random.nextFloat()*20 }%"/>
		</div>
		
	</c:forEach>
	
	</div>
	 
	<div style="margin-top:10%">
	
	<div class="fadeIn wow" data-wow-duration="3s" data-wow-delay="11s">
		<img class="row" src ="${pageContext.request.contextPath}/img/logo.png" style="margin:0 30% 0 30%;width: 40%" />
	</div>
	
	<div  class="fadeIn wow" data-wow-duration="2s" data-wow-delay="13s" align="center" style="visibility:hidden" >
		<p style="margin:1%;font-size: 2em;font-weight: 700">Coming Skhoon</p>
	</div>
	
	<div  class="fadeIn wow" data-wow-duration="2s" data-wow-delay="15s" align="center" style="visibility:hidden">
		<p style="font-size: 2em;font-weight: 700">Début 2019</p>
	</div>
	
	<div  class="fadeIn wow" data-wow-duration="2s" data-wow-delay="17s" align="center" style="visibility:hidden">
		<a rel="noopener" target="_blank" href="https://www.facebook.com/Boutique.Otaku/">
			<img alt="facebook" src="${pageContext.request.contextPath}/img/facebook_icon.png" style="width:32px;height:32px">	
		</a>
	</div>	
	 </div>
</body>
</html>
