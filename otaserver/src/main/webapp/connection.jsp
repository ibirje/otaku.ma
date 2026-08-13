<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
    <head>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css"/>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" media="all">
        <title>Connection</title>
        
        <script src='https://www.google.com/recaptcha/api.js'></script>
    </head>

    <body style="background-color:#CEECFF">
    
    	<div  style="height:15%;padding:20px 0;margin:0px" class="row" align="center"  >
    		<a href="${pageContext.request.contextPath}" class="pulsee">
				<img src="${pageContext.request.contextPath}/img/logo.png" alt="Otaku.ma" style="height: 50px; width:300px"/>
			</a>
    	</div>
    	
    	
		<div class="col-lg-offset-3 col-md-offset-3 col-sm-offset-2 col-xs-offset-1 
		col-lg-6 col-md-6 col-sm-8 col-xs-10 round-border">
			<div class="col-md-12" style="padding: 20px;">
			
				
				<div align="center">
				
	    		<c:if test="${not empty param.erreur}">
	    			<p style="color: red"> ${ param.erreur } </p>
				</c:if>
				
					<form action="${pageContext.request.contextPath}/authentification" method="post" >
						<input type="hidden" value="${source}" name="source" id="source">
						<div class="row" style="margin:10px 0">
							<input name="pseudo" type="text" class="form-control input-md" placeholder="Nom d'utilisateur ou email"  >
						</div>
						<div class="row" style="margin:10px 0">
							<input name="mdp" type="password" class="form-control input-md" placeholder="Mot de passe"  >
						</div>
						
						<div class="row" style="margin: 50px 0 5px 0">
							<a style="color:blue" rel="noopener" target="_blank" href="https://www.facebook.com/Boutique.Otaku/">
							
								Connecter avec Facebook
							
								<button class="footbutton" style="border:none;margin:0 20px 0 0">
									<picture>
										<source srcset="${pageContext.request.contextPath}/img/facebook_icon.webp" type="image/webp">
										<source srcset="${pageContext.request.contextPath}/img/facebook_icon.png" type="image/png"> 
										<img alt="facebook" src="${pageContext.request.contextPath}/img/facebook_icon.png" style="width:32px;height:32px">	
									</picture>
								</button>
							</a>
							
							<a href="${pageContext.request.contextPath}/" style="color: blue; margin: 0 0px 0 20px">
								Mot de passe oublié ? 
							</a>
						</div>
						
						<div class="row" style="margin: 5px 0 20px 0">
							Pas encore inscrit ? 
							
							<a style="color:blue" href="${pageContext.request.contextPath}/inscription">
								Inscriver-vous gratuitement
							</a>

						</div>
						
						<div class="g-recaptcha" data-sitekey="6LfOHXsUAAAAAFG-ZKmftSpZc4pNXoOKcrfb_vp8"></div>
						
						<div class="row"  style="padding: 10px;">
							<button class="btn acheternow center-block" value="Connecter" type="submit"> Connecter </button> 
						</div>
						
					</form>
				</div>
	    	</div>
    	</div>
    	
    </body>
</html>