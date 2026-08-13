import { CommandeService } from './../../services/commande.service';
import { FullPanier } from './../../data/restdata/FullPanier';
import { AuthenticationService } from './../../services/authentication.service';
import { HttpUrlEncodingCodec } from '@angular/common/http';

import { FullVariation } from './../../data/restdata/FullVariation';
import { FullAttribut } from './../../data/restdata/FullAttribut';
import { Component, OnInit, Input, Output, EventEmitter, Inject, ViewChild } from '@angular/core';
import { ProduitsService } from 'src/app/services/produits.service';
import { PanierItem } from 'src/app/data/PanierItem';
import { Router } from '@angular/router';
import { PopupComponent } from 'src/app/shared/popup/popup.component';

@Component({
  selector: 'app-variations-grid',
  templateUrl: './variations-grid.component.html',
  styleUrls: ['./variations-grid.component.css']
})
export class VariationsGridComponent implements OnInit {
  
  @ViewChild('ajoutpanierPopup') addpanierpop: PopupComponent;
  @ViewChild('nonConnectePopup') connectpopup: PopupComponent;
  @Input() produit;
  private _variations: [FullVariation];
  private _attributs: [FullAttribut];
   selectedcode;
  private _qte = 1;
  @Output() selectionChangedEvent = new EventEmitter();

  set qte(q) {
    if (q == null || q === undefined ||  q < 1 ) {
        this._qte = 1;
    } else {
      if ( q > 99 ) {
        this._qte = 99;
      } else {
        this._qte = q;
      }
    }
  }
  get qte() {
    return this._qte;
  }

  acheterClick() {
    if ( this.selectedcode != null ) {
      const p = new FullPanier();
      p.produit = this.produit;
      p.panier = {code : this.selectedcode, qte : this._qte};

      if ( this.variations != null ) {
        for ( const variation of this.variations) {
          if (variation.variation.code === this.selectedcode ) {
            p.variation = variation.variation;
          }
        }
      }

      
    this.passerCommande([p]);
    }
  }

  passerCommande( list: FullPanier[] ) {
    this.cmdservice.changeCommande(list);
    this.router.navigate(['/user/passercommande']);
  }

  ajouterPanierClick() {
    if ( this.selectedcode ) {

      this.service.addPanier({code : this.selectedcode, qte : this._qte}).subscribe(data => {
        this.addpanierpop.openPopup = true;
        if ( this.variations != null ) {
          for ( const variation of this.variations) {
            if (variation.variation.code === this.selectedcode ) {
              this.addpanierpop.text1 = variation.variation.nom + '     x     ' + this._qte;
              this.addpanierpop.image = variation.variation.image;
            }
          }
        } else {
          this.addpanierpop.text1 = 'produit ajouté au panier   x  ' + this._qte;
        }
      },
      Error => {
        this.connectpopup.openPopup = true;
        this.connectpopup.text1 = 'Connectez vous pour ajouter ce produit au panier';
      } );
    }
  }

  variationSelect(code) {
    this.selectedcode = code;
    this.selectionChangedEvent.emit(this.selectedcode);
  }
  @Input() public set variations(vr: [FullVariation] ) {
    this._variations = vr;
    
    if (this.variations == null) {
      this.selectedcode = this.produit.code; 
    }
  }
  public get variations(): [FullVariation] {
    return this._variations;
  }  

  @Input() public set attributs(vr: [FullAttribut] ) {
    this._attributs = vr;
  }
  public get attributs(): [FullAttribut] {
    return this._attributs;
  }
  goToPanier() {
    this.router.navigate(['/user/panier']);
  }
  goToLogin() {
    const codec = new HttpUrlEncodingCodec();
    this.router.navigate(['/user/login'], { queryParams: { returnUrl: codec.decodeKey(this.router.url.toString()) }});
  }
  constructor( private router: Router, private cmdservice: CommandeService, private service: ProduitsService) { }

  ngOnInit() {
  }

}

