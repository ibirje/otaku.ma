
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="col-md-12 col-lg-12 col-sm-12 col-xs-12 " id="footer">
	<div class="col-md-12  col-lg-12 col-sm-12 col-xs-12 " style=" padding-top:20px;padding-bottom:20px;">
	
			<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4"> 
				<h4><span style="color:gray">Assistance</span></h4>
				<a class="a1" href="#">Service client</a><br/>
	            <a class="a1" href="#">Litiges</a><br/>
	        	<a class="a1" href="#">Signaler un problème</a><br/>
			</div>
			
			<div class="col-lg-1 col-md-1 col-sm-2 col-xs-2">
				<a rel="noopener" target="_blank" href="https://www.facebook.com/Boutique.Otaku/">
					<button class="footbutton">
					
						<picture>
							<source srcset="${pageContext.request.contextPath}/img/facebook_icon.webp" type="image/webp">
							<source srcset="${pageContext.request.contextPath}/img/facebook_icon.png" type="image/png"> 
							<img alt="facebook" src="${pageContext.request.contextPath}/img/facebook_icon.png" style="width:48px;height:48px">	
						</picture>
					</button>
				</a>
			</div>
			
			<div class="col-lg-1 col-md-1 col-sm-2 col-xs-2"> 
				<a target="_blank" href="https://www.facebook.com/Boutique.Otaku/">
					<button class="footbutton">
						<picture>
							<source srcset="${pageContext.request.contextPath}/img/youtube_icon.webp" type="image/webp">
							<source srcset="${pageContext.request.contextPath}/img/youtube_icon.png" type="image/png"> 
							<img alt="youtube" src="${pageContext.request.contextPath}/img/youtube_icon.png" style="width:48px;height:48px">	
						</picture>
					</button>
				</a>
			</div> 
	</div>
</div>