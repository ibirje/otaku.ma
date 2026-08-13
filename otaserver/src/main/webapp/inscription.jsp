<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
    <head>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css"/>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" media="all">
        <title>Inscription</title>
        
        <script src='https://www.google.com/recaptcha/api.js'></script>
        
    </head>

    <body style="background-color:#CEECFF">
    
    	<div  style="height:15%;padding:20px 0;margin:0px" class="row" align="center" class="pulsee" >
    		<a href="/" >
				<img src="${pageContext.request.contextPath}/img/logo.png" alt="Otaku.ma" style="height: 50px; width:300px"/>
			</a>
    	</div>
    	
		<div class="col-lg-offset-3 col-md-offset-3 col-sm-offset-2 col-xs-offset-1 
		col-lg-6 col-md-6 col-sm-8 col-xs-10 round-border">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="padding: 20px;">
			
	    		
				
				<div align="center">
					<c:if test="${not empty param.erreur}">
		    			<p style="color: red"> ${ param.erreur } </p>
  						<c:remove var="erreur" scope="page" />
					</c:if>
				
					<form action="${pageContext.request.contextPath}/valideinscription" method="post" >
					
						<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 input-group" style="margin:10px 0">
							<input id="nom" name="nom" type="text" class="form-control" placeholder="Nom" style="width:49%;margin:0 2% 0 0" >
							<input id="prenom" name="prenom" type="text" class="form-control" placeholder="Prénom" style="width:49%" >
						</div>
						
						<div class="row" style="margin:10px 0">
							<input id="email" name="email" type="text" class="form-control input-md" placeholder="Adresse email"  >
						</div>
						
						<div class="row" style="margin:10px 0">
							<input id="mdp" name="mdp" type="password" class="form-control input-md" placeholder="Mot de passe"  >
						</div>
						<div class="row" style="margin:10px 0">
							<input id="mdp2" name="mdp2" type="password" class="form-control input-md" placeholder="Confirmez votre Mot de passe"  >
						</div>
						
						<div class="row" style="margin: 40px 0 10px 0">
							<a style="color:blue" rel="noopener" target="_blank" href="https://www.facebook.com/Boutique.Otaku/">
								S'inscrie avec Facebook
								<button class="footbutton" style="border:none">
									<picture>
										<source srcset="${pageContext.request.contextPath}/img/facebook_icon.webp" type="image/webp">
										<source srcset="${pageContext.request.contextPath}/img/facebook_icon.png" type="image/png"> 
										<img alt="facebook" src="${pageContext.request.contextPath}/img/facebook_icon.png" style="width:32px;height:32px">	
									</picture>
								</button>
							</a>
						</div>
						
						<div class="g-recaptcha" data-sitekey="6Ld8THsUAAAAAOmLFKTj-EQ0bGnldVS2Hrd7Ii0z"></div>
						
						<div class="row"  style="padding: 10px;">
							<button class="btn acheternow center-block" value="Confirmer" type="submit"> Confirmer </button> 
						</div>
						
					</form>
				</div>
	    	</div>
    	</div>
    	
    </body>
</html>