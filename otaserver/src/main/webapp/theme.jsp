<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="/error_theme_not_found.jsp" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    
    <title> ${theme.nom} et plus de goodies de mangas et jeux vidéos sur Otaku.ma</title>
    <!-- TODO categorie.getTitre() -->
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css" type="text/css" media="all"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/animate.min.css" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css"/>
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

<body style="background-color:#CEECFF">

	<jsp:include page="header.jsp" flush="true"></jsp:include>
	
	<div id="container">
	
	<div class="col-md-offset-1 col-lg-offset-1 col-md-10 col-lg-10 col-sm-12 col-xs-12" style="margin-top: 5px;">
		<c:forEach items="${branche_themes}" var="theme">
			<a href= ${ theme.link } >${theme.nom }</a> > 
	  	</c:forEach>
			<a href= ${ theme.link } >${theme.nom }</a>
	</div>
		
	<div class="col-md-offset-1 col-lg-offset-1 col-md-10 col-lg-10 col-sm-12 col-xs-12" style="margin-top: 5px;">
		<div class="col-md-12 col-lg-12 col-sm-12 col-xs-12 form-inline row">
			<div class="listefiltres" >
				<div class="btn-group btn-group-toggle" role="group" data-toggle="buttons">
                	<div class="btn-group"><label class=" control-label input-sm">Trier par :</label></div>
				
				  <label  id="labpertinence" class="btn btn-default">
				    <input type="radio" name="options" id="pertinence" onchange="filtre('pertinence')" 
				    autocomplete="off" >Pertinence <span class="caret" ></span>
				  </label>
				  <label id="labcommandes" class="btn btn-default">
				    <input type="radio" name="options" id="commandes" onchange="filtre('commandes')" autocomplete="off"> Commandes <span class="caret" ></span>
				  </label>
				  
				   <label id="labnotes" class ="btn btn-default">
				    <input type="radio" name="options" id="notes" onchange="filtre('notes')"  autocomplete="off"> Notes <span class="caret" ></span>
				  </label>
				  
				  <label id="labprix" class = "btn btn-default">
				    <input type="radio" name="options" id="prix" onchange="filtre('prix')"  autocomplete="off"> Prix 
				    <span class="caret caret-up" id="prixcaret"> </span>
				  </label>
				</div>
			</div>
			
			<form class="form-horizontal"  name="form" action="${theme.link}" method="get" id="form">
			
				<div class="listefiltres btn-group"  role="group">
				
                	<div class="btn-group"><label class=" control-label input-sm">Prix :</label></div>
					<div class="btn-group"><input type="text" value="${param.min}" class="form-control input-sm" placeholder="min" name="min" style="width:60px;"></div>
					<div class="btn-group"><input type="text" value="${param.max}" class="form-control input-sm" placeholder="max" name="max" style="width:60px;"></div>
					
                	<div class="btn-group"><label class=" control-label input-sm" style="width:80px;">Note min :</label></div>
					<div class="btn-group rating">
					   <!-- <input type="radio" id="star5" name="rating" value="5" /><label for="star5" title="Parfait!">5 stars</label> --> 
					   <% String rating = request.getParameter("rating");%>
					    <input type="radio" id="star4" name="rating" value="4" <% if(rating != null && rating.equals("4"))out.print("checked"); %> /><label for="star4" title="Très Bon">4 stars</label>
					    <input type="radio" id="star3" name="rating" value="3" <% if(rating != null && rating.equals("3"))out.print("checked"); %>/><label for="star3" title="Pas mal">3 stars</label>
					    <input type="radio" id="star2" name="rating" value="2" <% if(rating != null && rating.equals("2"))out.print("checked"); %>/><label for="star2" title="Moyen">2 stars</label>
					    <input type="radio" id="star1" name="rating" value="1" <% if(rating != null && rating.equals("1"))out.print("checked"); %>/><label for="star1" title="Mauvais">1 star</label>
					</div>
					
					<input type="hidden" id="filtre" name="filtre">
					<input type="hidden" id="nbpage" name="nbpage">
					
					<div class="btn-group" style="margin:0px 15px;">
						<button type="submit" id="formsubmit" class="btn okbtn">Ok</button>
					</div>
				</div>
			</form>
			
		</div>
	</div>
		
	<div class=" col-md-offset-1 col-lg-offset-1 col-md-10 col-lg-10 col-sm-12 col-xs-12">
		<div class="col-md-12 round-border">
			<c:forEach items="${prliste}" var="produit">
			 	<script>
			 	
			 		produitBox( "${ produit.link }" , "${ produit.thumbnail }","${ produit.nom }","${ produit.intPrixUnite }" ,
						"${ produit.reduc }","${ produit.intPrixPromo }" , "${ produit.stars }" , "${ produit.avis }" , "${ produit.commandes }" , "${ produit.ispromo }" );
						
				</script>
				
			</c:forEach>
		</div>
	</div>
	
	<div class="col-md-offset-1 col-lg-offset-1 col-md-10 col-lg-10 col-sm-12 col-xs-12" style="margin-bottom: 15px;">	
		<div class="btn-group btn-group-toggle" role="group" data-toggle="buttons" style="margin-bottom: 200px;float:right">
		
			<c:choose>
			    <c:when test="${page <= pagemin}">
					<label class ="btn btn-default disabled" ><input type="radio" name="options" autocomplete="off" ><</label>
			    </c:when>
			   <c:otherwise>
			   <label class ="btn btn-default" ><input type="radio" name="options" onchange='gopage(${page-1})' autocomplete="off" ><</label>
			   </c:otherwise>
		    </c:choose>	
			
			
			<c:forEach var="i" begin="${ pagemin }" end="${ pagemax }">
				<label class ="btn btn-default" id="page${i}"><input type="radio" name="options" onchange="gopage('${ i }')" autocomplete="off" >${i}</label>
			</c:forEach>
			
			<c:choose>
			    <c:when test="${page >= pagemax}">
					<label class ="btn btn-default disabled" ><input type="radio" name="options" autocomplete="off" >></label>
			    </c:when>
			   <c:otherwise>
			   <label class ="btn btn-default" ><input type="radio" name="options" onchange='gopage(${page+1})' autocomplete="off" >></label>
			   </c:otherwise>
		    </c:choose>	
		</div>
	</div>
</div>
<jsp:include page="footer.jsp"  flush="true"></jsp:include>

<script>
$(window).load(function()
{
	var filtre = "${param.filtre}" ;

	if(filtre == null || filtre == "" || filtre == "pertinence"){
		document.getElementById("labpertinence").className = "btn btn-default active";
	}
	if(filtre == "commandes"){
		document.getElementById("labcommandes").className = "btn btn-default active";
	}
	if(filtre == "notes"){
		document.getElementById("labnotes").className = "btn btn-default active";
	}
	if(filtre == "prix" || filtre =="prixDesc"){
		document.getElementById("labprix").className = "btn btn-default active";
	}

	if(filtre != null && filtre =="prixDesc"){
		document.getElementById("prixcaret").className ="caret";
	}

	if(filtre != null && filtre != "")
	{
		document.getElementById("filtre").value = filtre;
	}
	var page = "${param.nbpage}" ;
	if(page == "${page}")
	{
		document.getElementById("page${page}").className = "btn btn-default active";
	}
	else
	{
		document.getElementById("page1").className = "btn btn-default active";
	}
	var rating = "${param.rating}" ; //TODO
	
});
</script>

<script>

	$("#form").submit(function() {

		if(document.form.nbpage.value == "1")
		{
			document.form.nbpage.value="";
		}
		if(document.form.filtre.value == "pertinence")
		{
			document.form.filtre.value="";
		}
	    $(this).find(":input").filter(function () {
	        return !this.value;
	    }).attr("disabled", true);
	
	    return true;
	});

	function filtre( fl)
	{	
		if(fl == "prix")
		{
			if("${param.filtre}" == "prix")
			{
				fl = "prixDesc";
			}
		}
	    document.form.filtre.value = fl;
	    document.form.nbpage.value = "${param.nbpage}";
	    document.getElementById("formsubmit").click();
	}
	
	function gopage(page)
	{
		document.form.filtre.value = "${param.filtre}";
	    document.form.nbpage.value = page;
	    document.getElementById("formsubmit").click();
	}
</script>


</body>
</html>