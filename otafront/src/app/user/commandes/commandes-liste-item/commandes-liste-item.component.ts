import { ConfirmPopupComponent } from './../../../shared/confirm-popup/confirm-popup.component';
import { CommandeService } from './../../../services/commande.service';
import { Router } from '@angular/router';
import { FullCommande } from './../../../data/restdata/FullCommande';
import { Component, OnInit, Input, ViewChild, Output, EventEmitter } from '@angular/core';
import { ProduitsService } from 'src/app/services/produits.service';
import { PopupComponent } from 'src/app/shared/popup/popup.component';

@Component({
  selector: 'app-commandes-liste-item',
  templateUrl: './commandes-liste-item.component.html',
  styleUrls: ['./commandes-liste-item.component.css']
})
export class CommandesListeItemComponent implements OnInit {

  @Input() commande: FullCommande;
  @ViewChild('panierPopup') addpanierpop: PopupComponent;
  @ViewChild('annulerPopup') annulerpopup: ConfirmPopupComponent;
  @Output() detailsClick = new EventEmitter();
  
  constructor(private router: Router, private service: ProduitsService, private cmdservice: CommandeService) { }

  ngOnInit() {
  }
  ajouterPanier($event) {
    
    const code = $event;
    if ( !code ) { return; }

    console.log(code);
    this.service.addPanier( {code : code, qte : 1} ).subscribe(data => {
      this.addpanierpop.openPopup = true;
      if ( this.commande) {
        for ( const item of this.commande.items) {
          if (item.code === code ) {
            this.addpanierpop.text1 = item.nom + '     x     ' + 1;
            this.addpanierpop.image = item.thumbnail;
          }
        }
      } else {
        this.addpanierpop.text1 = 'produit ajouté au panier   x  ' + 1;
      }
    },
    Error => { });
  }

  goToPanier() {
    this.router.navigate(['/user/panier']);
  }
  annulerClick() {
    this.annulerpopup.openPopup = true;
    this.annulerpopup.text = 'Voulez vous annuler la commande \'' + this.commande.commande.code + '\' ?' ;
    this.annulerpopup.buttonText = 'Confirmer';
    this.annulerpopup.button2Text = 'Retour';
  }
  annulerBtn1Clicked() {
    this.cmdservice.annulerCommande({code : this.commande.commande.code})
    .subscribe(data => { this.commande.commande.etat = 'ANNULEE'; }, err => {});
    this.annulerpopup.openPopup = false;
  }
  annulerBtn2Clicked() {
    this.annulerpopup.openPopup = false;
  }
  showDetails() {
    this.detailsClick.emit(this.commande);
  }
}
