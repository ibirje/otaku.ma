<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//FR" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Profile</title>
        
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css" type="text/css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/animate.min.css" type="text/css"/>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" type="text/css" media="all">
        
        <script src="${pageContext.request.contextPath}/js/wow.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/fonctions.js"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        
        <script>
            wow = new WOW({ live:true })
            wow.init();
        </script>

    </head>
	<body style="background-color:#CEECFF">
		<jsp:include page="header.jsp" flush="true"></jsp:include>

		<div id="container" >
			<div id="main" style="margin-top:10px;margin-bottom:50px" align="center" class="col-md-12 col-lg-12 col-sm-12 col-xs-12">
			
				<div class="row" style="margin-bottom: 20px;margin-top: 10px">
					<div class="col-md-6 col-lg-4 col-xs-10 col-sm-10 col-md-offset-3 col-lg-offset-4 col-sm-offset-1 col-xs-offset-1 bordered " 
					 align="left"
					 style="padding:10px;margin-bottom:10px">
					 
						<div class="col-md-4 col-lg-4 col-xs-4 col-sm-4" align="center" style="background: rgb(98, 197, 255);width:100px;height:100px">
							Image
						</div>
						<div class="col-md-8 col-lg-8 col-xs-8 col-sm-8">
							<div>Classe : Débutant</div> 
							<div>Niveau : 1</div> 
							<div>Badges : 0</div> 
						</div>
					</div>
				</div>
			
			
				<div class="col-xs-6 col-sm-6 col-md-3 col-lg-3" style="padding:0px 5px " >
					<div class="bordered" style="border-color: rgb(0, 142, 230);background: rgb(118, 202, 255);padding:10px;margin-bottom: 10px">
						<h4 >
							<a href="#" style="color: rgb(32, 8, 97)">
								<span class="row"></span>
								<span class="row">Commandes</span>
								<span class="row">0</span>
							</a>
						</h4>
					 </div>
				</div>
				
				<div class="col-xs-6 col-sm-6 col-md-3 col-lg-3" style="padding:0px 5px " >
					<div class="bordered" style="border-color: rgb(0, 151, 8);background: rgb(137, 255, 143);padding:10px;margin-bottom: 10px">
						<h4 >
							<a href="#" style="color: rgb(8, 97, 20)">
								<span class="row"></span>
								<span class="row">Expéditions en attente</span>
								<span class="row">0</span>
							</a>
						</h4>
					 </div>
				</div>
				
				<div class="col-xs-6 col-sm-6 col-md-3 col-lg-3" style="padding:0px 5px " >
					<div  class="bordered" style="border-color: rgb(184, 89, 0);background: rgb(252, 186, 124);padding:10px;margin-bottom: 10px">
						<h4 >
							<a href="#" style="color: rgb(97, 61, 8)">
								<span class="row"></span>
								<span class="row">Livraisons en attente</span>
								<span class="row">0</span>
							</a>
						</h4>
					 </div>
				</div>
				
				<div class="col-xs-6 col-sm-6 col-md-3 col-lg-3"  style="padding:0px 5px "  >
					<div class="bordered" style="border-color: rgb(189, 0, 79);background: rgb(255, 140, 188);padding:10px;margin-bottom: 10px">
						<h4 >
							<a href="#" style="color: rgb(97, 8, 30)">
								<span class="row"></span>
								<span class="row">Messages</span>
								<span class="row">0</span>
							</a>
						</h4>
					 </div>
				</div>
				<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12" style="margin-top: 20px"></div>
				
				<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6" style="margin-bottom: 10px">
					<div class="bordered col-xs-12 col-sm-12 col-md-12 col-lg-12" style="border-color: #929292";background: white;">
						<h4  class="row" align="left" style="margin:5px 0px 0px 0px"> <span>Informations</span> </h4>	
						<hr style="margin:10px;border-color: #c0c0c0" />
						<h4  class="row" > <span>...</span></h4>
						<h4  class="row" > <span>...</span></h4>
						<h4  class="row" > <span>...</span></h4>
						<hr style="margin:10px;border-color: #c0c0c0" />
						<h4  class="row" > <a href="#"> <span>Modifier</span> </a></h4>
					</div>
				</div>
				
				<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6" style="margin-bottom: 10px">
					<div class="bordered col-xs-12 col-sm-12 col-md-12 col-lg-12" style="border-color: #929292";background: white;">
						<h4  class="row" align="left" style="margin:5px 0px 0px 0px"> <span>Adresses(0)</span> </h4>
						<hr style="margin:10px;border-color: #c0c0c0" />
						<h4  class="row" > <span>...</span></h4>
						<h4  class="row" > <span>...</span></h4>
						<h4  class="row" > <span>...</span></h4>
						<hr style="margin:10px;border-color: #c0c0c0" />
						<h4  class="row" > <a href="#"> <span>Modifier</span> </a></h4>
					</div>
				</div>
				
			</div>
		</div>
		
		<jsp:include page="footer.jsp" flush="true"></jsp:include>
		
	</body>
	
</html>