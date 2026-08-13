import { CountdownComponent } from './../../shared/countdown/countdown.component';
import { FullAttribut } from './../../data/restdata/FullAttribut';
import { FullVariation } from './../../data/restdata/FullVariation';
import { Component, OnInit, Input, ViewChild, SimpleChanges, OnChanges, EventEmitter, Output } from '@angular/core';
import { Produit } from 'src/app/data/Produit';

@Component({
  selector: 'app-produit-infos',
  templateUrl: './produit-infos.component.html',
  styleUrls: ['./produit-infos.component.css']
})
export class ProduitInfosComponent implements OnInit {

  private _produit: Produit;
  private _variations: [FullVariation];
  private _attributs: [FullAttribut];
  private datedebut;
  private datefin;
  private selectedCode;
  
  @ViewChild('appcountdown') counter: CountdownComponent;
  @Output() selectionChangedEvent = new EventEmitter();


  @Input() public set produit(pr: Produit) {
    this._produit = pr;

    if ( this._produit) {
      
      this._produit.isPromo = false;
      if (this._produit.stringDateDebutPromo != null && this._produit.stringDateFinPromo != null) {

        this.datedebut = this.parseDate (this._produit.stringDateDebutPromo);
        this.datefin   = this.parseDate (this._produit.stringDateFinPromo);

        if (this.datefin != null && this.datedebut != null) {
          const date = new Date();
          if (date >= this.datedebut && date < this.datefin) {
            this._produit.isPromo = true;
            this._produit.reduc = (this._produit.prixUnite - this._produit.prixPromo) / this._produit.prixUnite * 100;
          }
        }
      }
    }

  }
  
  public get produit(): Produit {
    return this._produit;
  }

  @Input() public set variations(vr: [FullVariation] ) {
    this._variations = vr;
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

  constructor() { }

  ngOnInit() {
  }

  promoFin() {
      this.produit.isPromo = false;
  }

  selectionChanged() {
    this.selectionChangedEvent.emit(this.selectedCode);
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
}
