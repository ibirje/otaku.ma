<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="/error_produit_not_found.jsp" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<html>
<head>
   
    <title>${produit.nom} dans ${branche_categories[0].nom} sur Otaku.ma</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css" type="text/css"/>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/produit.css" type="text/css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/animate.min.css" type="text/css"/>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" type="text/css" media="all">
    
    <script src="${pageContext.request.contextPath}/js/wow.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/fonctions.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    
	<script> wow = new WOW({ live:true }) wow.init(); </script>
    
</head>

<body style="background-color:#CEECFF;scrollbar-color:blue;">

<jsp:include page="header.jsp" flush="true"></jsp:include>

	<div class="col-md-offset-1 col-lg-offset-1 col-md-10 col-lg-10 col-sm-12 col-xs-12" style="margin-top: 5px;">
		<a href=${branche_categories[0].link } > ${branche_categories[0].nom}</a> > 
		<a href=${branche_categories[1].link } > ${branche_categories[1].nom}</a> > 
		<a href=${branche_categories[2].link } > ${branche_categories[2].nom}</a>
		<span style="margin-left:30px;margin-right:20px;"> dans </span><a href=${theme.link } > ${theme.nom} </a>
	</div>
	
<div  id="container" >
	<div id="main" class=" col-md-offset-1 col-lg-offset-1 col-md-10 col-lg-10 col-sm-12 col-xs-12">
	<div class="col-md-12 round-border" >
		<div id="produitslide" class="carousel slide col-md-5 col-lg-5 col-xs-12 col-sm-12" data-ride="carousel" style="padding-right: 0px; padding-left: 0px; margin-left: 0px; margin-right: 0px;">
			<ol class="carousel-indicators " style="left: 0px; right: 0px; width: 100%; margin: 0px;">
				<li data-target="#produitslide" data-slide-to="0" id="${produit.code }1" class="active">
					<img src="${produit.image1}" alt="" />
				</li>
				<c:set var="slide" value="1" scope="page"/>
				<c:if test="${not empty produit.image2}">
					<li data-target="#produitslide" data-slide-to="${slide}" id="${produit.code }2" >
						<img src="${produit.image2}" alt="" />
					</li>
					<c:set var="slide" value="${slide+1}" scope="page"/>
				</c:if>
				<c:if test="${not empty produit.image3}">
					<li data-target="#produitslide" data-slide-to="${slide}" id="${produit.code }3" >
						<img src="${produit.image3}" alt="" />
					</li>
					<c:set var="slide" value="${slide+1}" scope="page"/>
				</c:if>
					
				<c:forEach items="${variations}" var="variation">
					<c:if test="${not empty variation.image}">
						<li data-target="#produitslide" data-slide-to="${slide}" id="${variation.code}" >
							<img src="${variation.image}" alt="" />
						</li>
						<c:set var="slide" value="${slide+1}" scope="page"/>
					</c:if>
				</c:forEach>
					
			</ol>
			<div class="carousel-inner">
				<div class="item active"><img src="${produit.image1}" alt="${produit.nom }1" class="img-responsive"></div>
				<c:if test="${not empty produit.image2}">
					<div class="item"><img src="${produit.image2}"    alt="${produit.nom }2" class="img-responsive"></div>
				</c:if>
				<c:if test="${not empty produit.image3}">
					<div class="item"><img src="${produit.image3}"    alt="${produit.nom }3" class="img-responsive"></div>
				</c:if>
				<c:forEach items="${variations}" var="variation">
					<c:if test="${not empty variation.image}">
						<div class="item"><img src="${variation.image}" alt="${variation.nom }" class="img-responsive"></div>
					</c:if>
				</c:forEach>
			</div>
			<a class="left carousel-control" href="#produitslide" data-slide="prev">
				<span class="glyphicon glyphicon-chevron-left"></span>
				<span class="sr-only">Previous</span>
			</a> 
			<a class="right carousel-control" href="#produitslide" data-slide="next">
				<span class="glyphicon glyphicon-chevron-right"></span><span class="sr-only">Next</span>
			</a>
		</div>

  <!-- INFOS PRODUIT-->
		<div class="col-md-7 col-lg-7 col-sm-12 col-xs-12  animated fadeInRight wow"  data-wow-delay=".5s">
        	<div class="titre">
            	<h3 class="product-title" style="text-align: center"> ${ produit.nom } </h3>
                <div class="row avis" style="margin-left:5px">
                	<script>printStars( "${ produit.stars }" );</script>
                	<a data-toggle="tab" href="#comms" class="avis nbavis"> Avis(${ produit.avis }) </a>
                    <a data-toggle="tab" href="#comms" class="avis" > ${ produit.commandes } Commandes </a>
				</div>
			</div>
                
			<div class="col-md-12 col-lg-12 col-sm-12 col-xs-12">
            <c:choose><c:when test="${produit.countdown > 0}">
				<div style="margin-top:10px">
		        	Prix : <span class ="line-through gray" style="margin-left:20px">${produit.intPrixUnite} DH / pièce </span>
				</div> 
		        <div style="margin-top:10px; margin-bottom:10px;">
		        	Promotion Prix :
		        	<span class="promo"> ${ produit.intPrixPromo } DH </span> 
		            <span class="gray" > / pièce </span>
		            <span class="discount"> - ${ produit.reduc }% </span>
		            <span class="discount-countdown pull-right" id="discount"></span>
				</div>
			</c:when>
	        <c:otherwise>
		    	<div style="margin-top:20px; margin-bottom:10px;">
		        	Prix :
		        	<span class="promo"> ${ produit.intPrixUnite } DH </span> 
		            <span class="gray" > / pièce </span>
				</div>
			</c:otherwise></c:choose>
            <c:if test="${variations == null || variations.size() <= 0}">
            	<div class="noStock">
	            	<span style="vertical-align: middle;padding: 30px 0 0 30px">
			            Stock Epuisé
			            <img src="${pageContext.request.contextPath}/img/cancel.png" style="width:24px;height:24px;padding:4px;" alt="" />
	                </span>
				</div>
            </c:if>
            <c:if test="${variations.size()>0}">
	        	<c:forEach items="${options}" var="attribut">
		    		<div class="btn-group btn-group-toggle col-md-12 col-lg-12 col-sm-12 col-xs-12" style="margin-top:5px;margin-bottom:15px;" role="group" data-toggle="buttons" >
			        	<div class="btn-group"><label class="control-label input-sm attribut">${attribut.key} : </label></div>
			            <!-- ---------------------------------------------------------------------- -->
						<!-- khass input felform ytkteb fih variation.code bache ytajouta felpanier fe submit-->
						<c:choose><c:when test="${options.size() == 1}">
							<c:forEach items="${variations}" var="variation">
								<label id="lab${variation.code}" style="margin-right: 5px" class="btn btn-default variation" data-toggle="tooltip" title="${variation.nom}"> 
									<input type="radio"  name="${variation.nom}" value="${variation.nom}" autocomplete="off"> 
									<img src=${ variation.thumbnail } alt="${ variation.nom }" class="img-responsive variationimg" />
								</label>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<c:forEach items="${options[attribut.key]}" var="option">
								<label id="opt${option.code}" style="margin-right: 5px" class="btn btn-default"> 
									<input type="radio" onchange="selectOption('${option.code}')"name="${option.nom}" id="${option.nom}" value="${option.nom}" autocomplete="off">
									${option.nom}
								</label>
							</c:forEach>
						</c:otherwise></c:choose>
					</div>
				</c:forEach>
				
                <form action="/categorie.jsp" method="post" name="prodform">
    				<div style="margin-top:10px;margin-bottom:10px;">
	                	Quantité : 
	                    <input type="number" name="qte" value="1" min="1" max="67" style="margin:0px 10px; width:80px" >
	                    <span class="gray" style=" margin-right:20px;" >pièces</span>
	                </div>
					<div style="margin-top:20px; text-align: center">
	                	<button class="btn acheternow" value="acheter" type="submit">Acheter maintenant</button> 
	                	<button class="btn ajoutpanier" value="acheter" type="submit">Ajouter au panier</button> 
	                </div>
                </form>
			</c:if>
                
                <div class="col-md-12" style="margin-top:5px;text-align: center" >
                	<a href="#comms"><span class="gray" >❤  Ajouter à  la liste d'envies </span></a>
				</div>
            </div>
  		</div>
            <!-- NAV TABS MENU EN BAS -->
        <div class="col-md-12 col-lg-12 col-sm-12 col-xs-12">
			<ul class="nav nav-tabs">
            	<li class="active"><a data-toggle="tab" href="#details">Détails</a></li>
                <li><a data-toggle="tab" href="#comms"> Avis </a></li>
            </ul>
            <div class="tab-content">
                  <!-- DETAILS -->
            	<div id="details" class="tab-pane fade in active">
                      
                      <!-- SPECIFICATIONS -->
                	<div class="panel panel-default">
                        <div class="panel-heading">
                          <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#details" href="#specifications">
                            	Spécifications
                            </a>
                          </h4>
                        </div>
                        <div id="specifications" class="panel-collapse collapse in">
                          <div class="panel-body">
                               <div class="pull-left col-md-6 col-lg-6 col-sm-6 col-xs-6 fadeInRight wow" data-wow-delay=".2s">
                                   Categorie : ${branche_categories[2].nom} <br/>
                                   Theme : ${theme.nom }
                               </div>
                               <div class="pull-right col-md-6 col-lg-6 col-sm-6 col-xs-6 fadeInLeft wow" data-wow-delay=".2s">
                      		   <!-- Specifications depuis une table specification ou workaround attribut -->
                                   Modèle : BBC 300<br/>
                                   Dimensions : 3,2cm x 3,2 cm
                               </div>
                               <div class="pull-center col-md-10 col-lg-10 col-sm-10 col-xs-10 fadeInTop wow" data-wow-delay=".2s">
								<h3>${ produit.shortDescription}</h3>	
                               </div>
                          </div>
                        </div>
                        
                      </div>
                      
                      <!-- //SPECIFICATIONS -->
                      
                      <div class="panel panel-default">
                      	<div class="panel-heading">
                        	<h4 class="panel-title"><a data-toggle="collapse" data-parent="#details" href="#description">
                            Déscription du produit</a></h4>
                        </div>
                        <div id="description" class="panel-collapse collapse in">
                        	<div class="panel-body" style="text-align: center">
								<div class="pull-center col-md-12 col-lg-12 col-sm-12 col-xs-12 animated fadeInLeft wow" data-wow-delay=".2s">
									${ produit.description }
								</div>
								<img src=${ produit.image1 } alt="${produit.nom } 1" style="margin: 10px; border: 2px; border-radius: 2px; width: 90%" class="animated fadeInLeft wow" data-wow-delay=".2s" /> 
									
								<c:if test="${ not empty produit.image2 }">
										<img src=${ produit.image2 } alt="${produit.nom } 2" style="margin: 10px; border: 2px; border-radius: 2px; width: 90%" class="animated fadeInRight wow" data-wow-delay=".2s" /> 
								</c:if>
								<c:if test="${ not empty produit.image3 }">
										<img src=${ produit.image3 } alt="${produit.nom } 3" style="margin: 10px; border: 2px; border-radius: 2px; width: 90%" class="animated fadeInLeft wow" data-wow-delay=".2s" />
								</c:if>
								<c:forEach items="${variations}" var="variation">
									<c:if test="${not empty variation.image}">
										<img src=${ variation.image } alt="${variation.nom }" style="margin: 10px; border: 2px; border-radius: 2px; width: 90%" class="animated fadeInLeft wow" data-wow-delay=".2s" />
									</c:if>
								</c:forEach>
							</div>
                        </div>
                      </div>
                      
                      <div class="panel panel-default">
	                      <div class="panel-heading">
		                      <h4 class="panel-title">
			                      <a data-toggle="collapse" data-parent="#details" href="#suggestions">
			                      Produits similaires</a>
		                      </h4>
	                      </div>
                            <div class="clearfix"> </div>
                            <div id="suggestions" class="panel-collapse collapse in">
                            
                                <div class="panel-body">
                                
                                <c:forEach items="${prliste}" var="produit">
								 	<script>
									produitBox( "${ produit.link }" , "${ produit.thumbnail }","${ produit.nom }","${ produit.intPrixUnite }" ,
									"${ produit.reduc }","${ produit.intPrixPromo }" , "${ produit.stars }" , "${ produit.avis }" , "${ produit.commandes }" , "${produit.ispromo}" );
									</script>
								</c:forEach>
                                    
                                </div>
                			</div>
                  	</div>
               	  </div> 
                    <!-- AVIS -->
                	<div id="comms" class="tab-pane fade in">
                        <h3>Avis</h3>
                        <p>Hassan : az eaz aze a aze a aze .</p>
                        <p>Samir :zae az eaz aze a aze a aze .</p>
                        <p>Kamal :zae az eaz aze a aze a aze .</p>
                        <p>apzeok : zae az eaz aze a aze a aze .</p>
                    </div>
				</div>
			</div>
		</div>  
	</div>
</div>
	<script>
		$(document).ready(function(){
	    	
	    	var time = parseInt( "${produit.countdown}" );
			var display = document.querySelector('#discount');
	        startTimer(time, display);
	        if(time <= 0)
	        {
	        	document.getElementById("discount").style.display = 'none';
	        }
	    });
    </script>
    
	<script>
		function selectOption(ocode) {
			alert(ocode);
		}
		function varSelectCallBack(str)
		{
			return function(){
				var vcode = str;
				var nb = parseInt(document.getElementById(vcode).getAttribute('data-slide-to'));
			    $("#produitslide").carousel(nb);
			    $("#produitslide").carousel("pause");
			}
		}
	</script>
	
	<c:forEach items="${variations}" var="variation">
	<script>
		$(document).ready(function(){
			$("#lab${variation.code}").click( varSelectCallBack('${variation.code}'));
		});
	</script>
	</c:forEach>
    
</body>
<footer>
	<jsp:include page="footer.jsp" flush="true"></jsp:include>
</footer>


</html>
