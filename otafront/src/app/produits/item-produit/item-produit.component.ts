import { HttpUrlEncodingCodec } from '@angular/common/http';
import { CommandeService } from './../../services/commande.service';
import { Router } from '@angular/router';
import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { Produit } from '../../data/Produit';
import { ProduitsService } from 'src/app/services/produits.service';
import { PopupComponent } from 'src/app/shared/popup/popup.component';

@Component({
  selector: 'app-item-produit',
  templateUrl: './item-produit.component.html',
  styleUrls: ['./item-produit.component.css']
})
export class ItemProduitComponent extends Produit implements OnInit {

  @ViewChild('panierPopup') addpanierpop: PopupComponent;
  @ViewChild('nonConnectePopup') connectpopup: PopupComponent;

  loaded = false;

  isloaded() {
    this.loaded = true;

    this.isPromo = false;
    if (this.stringDateDebutPromo != null && this.stringDateFinPromo != null) {

      this.dateDebutPromo = this.parseDate(this.stringDateDebutPromo);
      this.dateFinPromo = this.parseDate(this.stringDateFinPromo);

      if (this.dateFinPromo != null && this.dateDebutPromo != null) {
        const date = new Date();
        if (date >= this.dateDebutPromo && date < this.dateFinPromo) {
          this.isPromo = true;
          this.reduc = (this.prixUnite - this.prixPromo) / this.prixUnite * 100;
        }
      }
    }
  }

  parseDate(value: any): Date | null {
    if ((typeof value === 'string') && (value.indexOf('/') > -1)) {
      const str = value.split(' ')[0].split('/');

      const year = Number(str[2]);
      const month = Number(str[1]) - 1;
      const date = Number(str[0]);
      const heure = Number(value.split(' ')[1].trim().split(':')[0]);

      return new Date(year, month, date, heure);
    }
    return new Date();
  }
  constructor(private router: Router, private service: ProduitsService, private cmdservice: CommandeService) {
    super();
  }

  ajouterPanier() {

    this.service.addPanier({ code: this.code, qte: 1 }).subscribe(data => {
      this.addpanierpop.openPopup = true;
      if (this.code) {
        this.addpanierpop.text1 = this.nom + '     x     ' + 1;
        this.addpanierpop.image = this.thumbnail;
      } else {
          this.addpanierpop.text1 = 'produit ajouté au panier   x  ' + 1;
        }
      },
      Error => {
        this.connectpopup.openPopup = true;
        this.connectpopup.text1 = 'Connectez vous pour ajouter ce produit au panier';
      } );
  }


  goToPanier() {
    this.router.navigate(['/user/panier']);
  }

  goToLogin() {
    const codec = new HttpUrlEncodingCodec();
    this.router.navigate(['/user/login'], { queryParams: { returnUrl: codec.decodeKey(this.router.url.toString()) }});
  }
  ngOnInit() {
  }

}
