<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//FR" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Title</title>
        
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" type="text/css" media="all">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css" type="text/css"/>
    	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/indexcomingsoon.css" type="text/css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/animate.min.css" type="text/css"/>
        
        <script src="${pageContext.request.contextPath}/js/wow.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/fonctions.js"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        
        <script>
            wow = new WOW({ live:true })
            wow.init();
        </script>

    </head>

    <body style="background:rgb(250, 250, 250)">
    
        <jsp:include page="header.jsp" flush="true"></jsp:include>

		<div  id="container" >
			<div id="main" style="margin-top:10px;margin-bottom:50px" align="center" class="col-md-12 col-lg-12 col-sm-12 col-xs-12">
			

			<div class="hidden-xs col-sm-3 col-md-3 col-lg-2" >
			
				<div style="background: rgb(0, 155, 252);padding:10px;color: white;margin-bottom: 10px">
					<h4 class="panel-title">
						<span>Catégories</span>
					</h4>
				 </div>
			<div class="panel-group">
				<c:forEach items="${categories}" var="categ">
					<c:if test="${ empty categ.categorieParent }">
						
					  <div class="panel panel-default">
					    <div class="panel-heading">
					      <h4 class="panel-title">
					        <a href=${ categ.link }> <span>${ categ.nom }</span> </a>
					        <a data-toggle="collapse" href="#${ categ.code }"><span class="caret"></span></a>
					      </h4>
					    </div>
					    <div id="${ categ.code}" class="panel-collapse collapse in">
							<ul class="list-group">
								<c:forEach items="${categories}" var="fils1">
									<c:if test="${ not empty fils1.categorieParent and fils1.categorieParent == categ.categorieID  }">
								        <li class="list-group-item" style="border: none">
											<div class="panel-group">
											  <div class="panel panel-default" style="border: none">
											  
											    <div class="panel-heading">
											      <h4 class="panel-title">
											        <a href="${fils1.link}">${fils1.nom}</a>
											        <a data-toggle="collapse" href="#${ fils1.code}"><span class="caret"></span></a>
											      </h4>
											    </div>
											    
											    <div id="${fils1.code}" class="panel-collapse collapse in">
											      <ul class="list-group">
													<c:forEach items="${categories}" var="fils2">
														<c:if test="${ not empty fils2.categorieParent and fils2.categorieParent == fils1.categorieID  }">
									    					<li class="list-group-item" style="border: none">
													        	<a href="${fils2.link}">${fils2.nom}</a>
													        </li>
													    </c:if>
											        </c:forEach>
											      </ul>
											    </div>
											    
											  </div>
											</div>
								        </li> 
							        </c:if>
						        </c:forEach>
							</ul>			      
					    </div>
					  </div>
					</c:if>
				</c:forEach>
				</div>
				
			</div> 
			
			<div class="col-md-6 col-sm-6 col-lg-8">
			
		        <div id="myCarousel" class="carousel slide " data-ride="carousel">
	
					<ol class="carousel-indicators">
						<li data-target="#myCarousel" data-slide-to="0" class="active"></li>
						<li data-target="#myCarousel" data-slide-to="1"></li>
					</ol>
					
					<div class="carousel-inner">
						<div class="item active">
							<a href="${pageContext.request.contextPath}/themes/Jeux-Vidéo">
								<img src="${pageContext.request.contextPath}/img/promonoel.png" style="width:100%;height:100%" alt="Promotions Noel">
							</a>
						</div>
						
						<div class="item">
							<a href="${pageContext.request.contextPath}/themes/Mangas">
								<img src="${pageContext.request.contextPath}/img/promoonichan.png" style="width:100%;height:100%" alt="Promotions Manga">
							</a>
						</div>
					</div>
					
					<a class="left carousel-control" href="#myCarousel" data-slide="prev" style="background-image:none !important;filter:none !important" >
						<span class="glyphicon glyphicon-chevron-left"></span>
						<span class="sr-only">Previous</span>
					</a>
					  
					<a class="right carousel-control" href="#myCarousel" data-slide="next" style="background-image:none !important;filter:none !important">
						<span class="glyphicon glyphicon-chevron-right"></span>
						<span class="sr-only">Next</span>
					</a>
					  
				 </div>
				 <div style="margin-top: 20px">
				 
					    <div style="background: rgb(0, 155, 252);padding: 10px;color: white;margin-bottom: 10px">
					    	<h4 class="panel-title">
						    	<span>Produits à la une</span>
						    </h4>
					    </div>
				 
				 
					<c:forEach items="${prodrec}" var="produit">
						<script>
							produitBox2( "${ produit.link }" , "${ produit.thumbnail }","${ produit.nom }","${ produit.intPrixUnite }" ,
							"${ produit.reduc }","${ produit.intPrixPromo }" , "${ produit.stars }" , "${ produit.avis }" , "${ produit.commandes }" , "${produit.ispromo}" );
						</script>
					</c:forEach>
				 </div>
 			</div>
			 
			<div class="hidden-xs col-sm-3 col-md-3 col-lg-2" >
			
				<div style="background: rgb(0, 155, 252);padding:10px;color: white;margin-bottom: 10px">
					<h4 class="panel-title">
						<span>Thèmes</span>
					</h4>
				 </div>
			
				<div class="panel-group">
					<c:forEach items="${themes}" var="theme">
						<c:if test="${ empty theme.themeParent }">
							
						  <div class="panel panel-default">
						    <div class="panel-heading">
						      <h4 class="panel-title">
						        <a href=${ theme.link }> <span>${ theme.nom }</span> </a>
						        <a data-toggle="collapse" href="#${ theme.code }"><span class="caret"></span></a>
						      </h4>
						    </div>
						    <div id="${ categ.code}" class="panel-collapse collapse in">
								<ul class="list-group">
									<c:forEach items="${themes}" var="fils2">
										<c:if test="${ not empty fils2.themeParent and fils2.themeParent == theme.themeID  }">
											<li class="list-group-item" style="border: none">
												<a href="${fils2.link}">${fils2.nom}</a>
											</li>
										</c:if>
									</c:forEach>
								</ul>			      
						    </div>
						  </div>
						</c:if>
					</c:forEach>
				</div>
				
				 <div  align="center" style="background : #CEECFF">
				 
					 <div style="background: rgb(0, 155, 252);padding: 10px;color: white;margin-bottom: 10px;margin-top: 20px">
						<h4 class="panel-title">
							<span>Jouez au tombola</span>
						</h4>
					 </div>
					 
					 <a href="#" >
					 	<span>Vous avez des coupons !</span>
					 	<img  style="padding: 10px 30px 15px 30px;background : #CEECFF" class="hidden-xs col-sm-12 col-md-12 col-lg-12" alt="Coupons" src="${pageContext.request.contextPath}/img/coupon.png" />
					 </a>
				 </div>
			</div> 
				
	        </div>
 		</div>
		<jsp:include page="footer.jsp" flush="true"></jsp:include>
		
    </body>

</html>