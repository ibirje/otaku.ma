
function printStars(nb)
{

	var starcc = "<span class=\"checked\">★</span>";
	var starnc = "<span class=\"notchecked\">★</span>";
	var str="";
	for(var i = 0 ; i < nb ; i ++)
	{
		str = str.concat(starcc);
	}
	for(var j = 0 ; j < 5-nb ; j ++)
	{
		str = str.concat(starnc);
	}
	document.write(str);
}


function produitBox(lien,img,titre,prix,reduc,promo,etoiles,nbavis,nbcmd,ispromo)
{
	document.write("<div class= \"col-md-4 col-lg-3 col-sm-6 col-xs-12\" >");
	document.write("<div class=\"col-md-12 col-lg-12 col-sm-12 col-xs-12 produit_liste fadeInRight wow\" data-wow-delay=\".3s\">");
	document.write("<a href=\""+lien+"\"> <img src=\""+img+"\" alt=\""+titre+"\"  title=\""+titre+"\"  class=\"col-md-12 col-lg-12 col-sm-12 col-xs-12\" />  </a>");
	
	document.write("<div class=\"col-md-12 col-lg-12 col-sm-12 col-xs-12 \"><center><a href=\""+lien+"\"> " +
	"<span class=\"produit_titre\" title=\""+titre+"\"> "+titre+"</span> </a></center></div>"+
	"<div class=\"row\"><center>");
	if(ispromo == 'true')
	{
		document.write(" <span class =\"line-through gray\" style=\"margin-left:20px\">"+prix+"DH</span> ");
		document.write("<span class=\"promo\"> "+promo+" DH </span>");
		document.write("<span class=\"discount\"> -"+reduc+"% </span>");
	}
	else
	{
		document.write("<span class=\"promo\"> "+prix+" DH </span>");
	}
	document.write(" </center></div>");
	
	document.write("<div class=\"col-md-12 col-lg-12 col-sm-12 col-xs-12\"> <center> ");
	printStars(etoiles);
	document.write(" <a href=\"#comms\"> <span class=\"nbavis\">("+nbavis+")</span></a>" 
	+"<a  href=\"#comms\"> <span class=\"avis\" >Commandes("+nbcmd+")</span></a>" 
	+"</center>"
	+"</div>"+
	"<div class=\"col-md-12 col-lg-12 col-sm-12 col-xs-12\"> <center> <a href=\"#comms\" > <span class=\"gray\">" + 
	"❤ Ajouter à la liste d'envies" + 
	"</span> </a> </center> </div>");
	
	document.write("</div></div>");
}

function produitBox2(lien,img,titre,prix,reduc,promo,etoiles,nbavis,nbcmd,ispromo)
{
	document.write("<div class= \"col-md-4 col-lg-4 col-sm-12 col-xs-12\" >");
	document.write("<div class=\"col-md-12 col-lg-12 col-sm-12 col-xs-12 produit_liste fadeInRight wow\" data-wow-delay=\".3s\">");
	document.write("<a href=\""+lien+"\"> <img src=\""+img+"\" alt=\""+titre+"\"  title=\""+titre+"\"  class=\"col-md-12 col-lg-12 col-sm-12 col-xs-12\" />  </a>");
	
	document.write("<div class=\"col-md-12 col-lg-12 col-sm-12 col-xs-12 \"><center><a href=\""+lien+"\"> " +
	"<span class=\"produit_titre\" title=\""+titre+"\"> "+titre+"</span> </a></center></div>"+
	"<div class=\"row\"><center>");
	if(ispromo == 'true')
	{
		document.write(" <span class =\"line-through gray\" style=\"margin-left:20px\">"+prix+"DH</span> ");
		document.write("<span class=\"promo\"> "+promo+" DH </span>");
		document.write("<span class=\"discount\"> -"+reduc+"% </span>");
	}
	else
	{
		document.write("<span class=\"promo\"> "+prix+" DH </span>");
	}
	document.write(" </center></div>");
	
	document.write("<div class=\"col-md-12 col-lg-12 col-sm-12 col-xs-12\"> <center> ");
	printStars(etoiles);
	document.write(" <a href=\"#comms\"> <span class=\"nbavis\">("+nbavis+")</span></a>" 
	+"<a  href=\"#comms\"> <span class=\"avis\" >Commandes("+nbcmd+")</span></a>" 
	+"</center>"
	+"</div>"+
	"<div class=\"col-md-12 col-lg-12 col-sm-12 col-xs-12\"> <center> <a href=\"#comms\" > <span class=\"gray\">" + 
	"❤ Ajouter à la liste d'envies" + 
	"</span> </a> </center> </div>");
	
	document.write("</div></div>");
}


function startTimer(duration, display) 
{
	"use strict";
    var timer = duration, minutes, seconds, heures, jours;
	
    setInterval(function () {

		jours  = parseInt(timer / (3600*24), 10);
		heures  = parseInt((timer%(3600*24)) / 3600, 10);
        minutes = parseInt((timer%3600)/ 60, 10);
        seconds = parseInt(timer % 60, 10);

        jours = jours < 10 ? "0" + jours : jours;
        heures = heures < 10 ? "0" + heures : heures;
        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        display.textContent =jours + "j" + heures + "h" + minutes + "m" + seconds +"s";

        if (--timer < 0) {
            timer = duration;
        }
    }, 1000);
}


window.onload = function () {
	"use strict";
	$('.topnav').css("animation","fadeOutUp 1s forwards");
};