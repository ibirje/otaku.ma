
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="topnav" style="visibility:hidden;">

    	<a href="#accueil">
    	<picture>
			<source srcset="${pageContext.request.contextPath}/img/logo.webp" type="image/webp">
			<source srcset="${pageContext.request.contextPath}/img/logo.png" type="image/png"> 
    		<img src="${pageContext.request.contextPath}/img/logo.png" alt="logo" width="140" class="logo" />
		</picture>
    	
    	</a>
        <div class="dropdown">
            	<button class="btn dropdown-toggle" type="button" data-toggle="dropdown">Catégories
            	<span class="caret"></span></button>
            	<ul class="dropdown-menu multi">
                        <div class="col-md-4 col-lg-4 col-sm-4 col-xs-4" >
                            <ul class="multi-column-dropdown">
                                <h6> Accessoires et bijoux </h6>
                                <li><a href="#">Bagues</a></li>
                                <li><a href="#">Bracelets</a></li>
                                <li><a href="#">Pendentifs</a></li>
                            </ul>
                        </div>
                        
                        <div class="col-md-4  col-lg-4 col-sm-4 col-xs-4">
                            <ul class="multi-column-dropdown">
                                <h6> Figurines et jouets </h6>
                                <li><a href="#">Figurines</a></li>
                                <li><a href="#">Puzzle</a></li>
                                <li><a href="#">Peluches</a></li>
                            </ul>
                        </div>
                        
                        <div class="col-md-4 col-lg-4 col-sm-4 col-xs-4">
                            <ul class="multi-column-dropdown">
                                <h6> Mode </h6>
                                <li><a href="#">T shirts</a></li>
                                <li><a href="#">Chapeaux et casquettes</a></li>
                                <li><a href="#">Capuches et vestes </a></li>
                            </ul>
                        </div>
              	</ul>
			</div> 

          	<div class="dropdown">   
                <button class="btn dropdown-toggle" type="button" data-toggle="dropdown">Themes
                <span class="caret"></span></button>
                <ul class="dropdown-menu multi">
                    <div class="row">
                        <div class="col-md-6  col-lg-6 col-sm-6 col-xs-6">
                            <ul class="multi-column-dropdown">
                                <h6>Anime </h6>
                                <li><a href="#">Dragon Ball</a></li>
                                <li><a href="#">Naruto</a></li>
                                <li><a href="#">One Piece</a></li>
                                <li><a href="#">Gintama</a></li>
                                <li><a href="#">Boku No Hero Academia</a></li>
                            </ul>
                        </div>
                        
                        <div class="col-md-6  col-lg-6 col-sm-6 col-xs-6">
                            <ul class="multi-column-dropdown">
                                <h6> Jeux Vidéo </h6>
                                <li><a href="#">Hearthstone</a></li>
                                <li><a href="#">League Of Legends</a></li>
                                <li><a href="#">Street Fighter</a></li> 
                                <li><a href="#">Super Mario</a></li>
                            </ul>
                    	</div>
                	</div>
            	</ul>
            </div> 
          	<div style="float:right">
                <button id="panier" class="panier" href="#panier" >
						<picture>
							<source srcset="${pageContext.request.contextPath}/img/chest.webp" type="image/webp">
							<source srcset="${pageContext.request.contextPath}/img/chest.png" type="image/png"> 
	                          <img src="${pageContext.request.contextPath}/img/chest.png" alt="chercher" class="pan" alt="panier" width="34" height="34"  />
						</picture>
                <span class="label label-danger" 
                    style="margin-left:-15px; border:2px; border-radius:8px;">7</span>				
                </button>
                  
                <div class="search-container">
                    <form action="/action_page.php">
                    	<div class="btn-group" role="group" >
                    	
                			<div class="btn-group">
	                        	<input class="navbartext" type="text" placeholder="Rechercher.." name="search">
	                        </div>
	                        <button type="submit" class="searchbutton">
							<picture>
								<source srcset="${pageContext.request.contextPath}/img/search.webp" type="image/webp" />
								<source srcset="${pageContext.request.contextPath}/img/search.png" type="image/png" /> 
		                          <img src="${pageContext.request.contextPath}/img/search.png" alt="chercher" width="24" height="24" class="searchimg" />
							</picture>
	                        </button>
                        </div>
                    </form>
            	</div>
          	</div>
</div>
<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id="banner"  
	style="background-image:url(
	<c:if test="${not empty banner}">${banner}</c:if>
	<c:if test="${empty banner}">${pageContext.request.contextPath}/img/banner2.jpg</c:if>
	)">

<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-top:10px">
    
	<div class="searchbar"  > 
        <div class="search-container">
            <form action="/search">
            	<div class="btn-group " role="group" aria-label="...">
                	<div class="btn-group">
                  		<input class="navbartext" type="text" placeholder="Rechercher.." name="search">
                	</div>
                    <div class="btn-group">
                        <button class="drop" type="button" data-toggle="dropdown">
                        	Themes<span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu multi">
                            <div class="row">
                                <div class="col-md-6  col-lg-6 col-sm-6 col-xs-6">
                                    <ul class="multi-column-dropdown">
                                        <h6>Anime </h6>
                                        <li><a href="#">Dragon Ball</a></li>
                                        <li><a href="#">Naruto</a></li>
                                        <li><a href="#">One Piece</a></li>
                                        <li><a href="#">Gintama</a></li>
                                        <li><a href="#">Boku No Hero Academia</a></li>
                                    </ul>
                                </div>
                            
                                <div class="col-md-6  col-lg-6 col-sm-6 col-xs-6">
                                    <ul class="multi-column-dropdown">
                                        <h6> Jeux Vidéo </h6>
                                        <li><a href="#">Hearthstone</a></li>
                                        <li><a href="#">League Of Legends</a></li>
                                        <li><a href="#">Street Fighter</a></li> 
                                        <li><a href="#">Super Mario</a></li>
                                    </ul>
                                </div>
                            </div>
                        </ul>
					</div>
                    <div class="btn-group">
                            <button class=" drop " type="button" data-toggle="dropdown">
                                Catégories<span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu multi">
                                <div class="row">
                                    <div class="col-md-4 col-lg-4 col-sm-4 col-xs-4" >
                                        <ul class="multi-column-dropdown">
                                            <h6> Accessoires et bijoux </h6>
                                            <li><a href="#">Bagues</a></li>
                                            <li><a href="#">Bracelets</a></li>
                                            <li><a href="#">Pendentifs</a></li>
                                        </ul>
                                    </div>
                                    
                                    <div class="col-md-4  col-lg-4 col-sm-4 col-xs-4">
                                        <ul class="multi-column-dropdown">
                                            <h6> Figurines et jouets </h6>
                                            <li><a href="#">Figurines</a></li>
                                            <li><a href="#">Puzzle</a></li>
                                            <li><a href="#">Peluches</a></li>
                                        </ul>
                                    </div>
                                    
                                    <div class="col-md-4  col-lg-4 col-sm-4 col-xs-4">
                                        <ul class="multi-column-dropdown">
                                            <h6> Mode </h6>
                                            <li><a href="#">T shirts</a></li>
                                            <li><a href="#">Chapeaux et casquettes</a></li>
                                            <li><a href="#">Capuches et vestes </a></li>
                                        </ul>
                                    </div>
                                </div>
                            </ul>
                    </div>
                    <button type="submit" class=" srchbutton">
						<picture>
							<source srcset="${pageContext.request.contextPath}/img/search.webp" type="image/webp">
							<source srcset="${pageContext.request.contextPath}/img/search.png" type="image/png"> 
	                          <img src="${pageContext.request.contextPath}/img/search.png" alt="chercher" width="32" height="32" class="searchimg" />
						</picture>
                    </button>
            	</div>
            </form>
        </div>
    </div>
    
	<div class="dropdown bordered" style="margin-top: 0px;" >
    	<!-- 
		${_connected}
    	${_nom}
		${_prenom}
		${_paniercount}
    	-->
    	<c:choose>
    		<c:when test="${not empty cookie and not empty cookie.USER_TOKEN}">
		    	<div class="btn-group " role="group" aria-label="...">
			    	<a href="#" class="btn connectbtn btnp1" >${cookie._nom.value} ${cookie._prenom.value}</a>
			        	<a href="${pageContext.request.contextPath}/deconnecter"  class="btn connectbtn btnp2">
				            <span style="padding-right:2px;"> Deconnecter</span> 
				            <span class="caret" ></span>
			            </a>
		    	</div>
				<div class="dropdown-content ">
					<a class="a2" href="#">Messages</a>
					<a class="a1" href="#">Commandes</a>
					<a class="a1" href="#">Contacts</a>
				</div>
	    	</c:when>
	    	<c:otherwise>
	    		<div class="btn-group " role="group" aria-label="...">
		    		<a href="${pageContext.request.contextPath}/connection" class="btn connectbtn btnp1" >Se connecter</a>
			        <a href="${pageContext.request.contextPath}/inscription"  class="btn connectbtn btnp2">
					    <span style="padding-right:2px;"> S'inscrire</span> 
					    <span class="caret" ></span>
			        </a>
				</div>      
			        <div class="dropdown-content ">
			        	<a class="a2" href="${pageContext.request.contextPath}/connection">Messages</a>
			            <a class="a1" href="${pageContext.request.contextPath}/connection">Commandes</a>
			            <a class="a1" href="#">Contacts</a>
					</div>
		            
	    		 </c:otherwise>
    		 </c:choose>
    		 
        
		
	</div>
</div>
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 fav_panier" >
		<button class="panier" href="#panier" >
			<div>
				<picture>
					<source srcset="${pageContext.request.contextPath}/img/chest.webp" type="image/webp">
					<source srcset="${pageContext.request.contextPath}/img/chest.png" type="image/png" > 
					<img src="${pageContext.request.contextPath}/img/chest.png" class="pan" alt="panier" width="34" height="34"  />
				</picture>
				<span class="label label-danger" style="margin-left:-15px; border:2px; border-radius:8px;">${cookie._paniercount.value}</span>	
			</div>
			<div> <span class="paniertext"> Panier </span> </div>	
	    </button>  
	    <button class="favoris" href="#favoris" >
	    	<div ><span class="heartz">❤ </span></div>
	        <div><span class="favtext"> Favoris</span></div>
		</button> 
	</div>
		<span style="float: right;color:white;margin-right: 30px;font-size:20px;" class="fadeInRight wow"  data-wow-delay="1s"> 
			${titre}
		</span>
</div>
<script>
	jQuery(function($)
	{
		"use strict";
		$(window).scroll(function()
		{
			if( $(this).scrollTop() < 200 ) 
			{
				$('.topnav').css("animation","fadeOutUp 1s forwards");
			}
			else if ($(this).scrollTop() > 200 ) 
			{
				$('.topnav').css("visibility","visible");
				$('.topnav').css("animation","fadeInDown 1s");
			}
		});
	});
</script>

      