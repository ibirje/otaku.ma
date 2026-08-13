import { Categorie } from './../../data/Categorie';
import { Component, OnInit, Input } from '@angular/core';
import { Produit } from 'src/app/data/Produit';
import { Theme } from 'src/app/data/Theme';

@Component({
  selector: 'app-produit-specifications',
  templateUrl: './produit-specifications.component.html',
  styleUrls: ['./produit-specifications.component.css']
})
export class ProduitSpecificationsComponent implements OnInit {

  private _produit: Produit;
  @Input() categories: [Categorie];
  @Input() theme: Theme;
  specsLeft: string[];
  specsRight: string[];

  @Input() set produit(prod: Produit) {
    this._produit = prod;
    if (this._produit) {
      this.specsLeft = [];
      this.specsRight = [];
      const speclist = this._produit.shortDescription.split(';');
      
      if ( this.categories != null) {
        const categ = this.categories[this.categories.length - 1];
        this.specsLeft.push('Categorie : ' + categ.nom);
      }
      if ( this.theme != null) {
        this.specsRight.push('Theme : ' + this.theme.nom);
      }
      let side = true;
      for ( const spec of speclist) {
        if ( side ) {
          this.specsLeft.push(spec);
        } else {
          this.specsRight.push(spec);
        }
        side = !side;
      }
    }
  }
  get produit() {
    return this._produit;
  }
  

  constructor() { }

  ngOnInit() {
  }

}
