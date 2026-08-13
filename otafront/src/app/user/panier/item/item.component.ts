import { FullPanier } from './../../../data/restdata/FullPanier';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-item',
  templateUrl: './item.component.html',
  styleUrls: ['./item.component.css']
})
export class ItemComponent implements OnInit {

  private _item: FullPanier;
  prix = 0;
  minQte = 1;
  @Output() public produitPrix = 0;
  @Output() public qte = 1;
  _selected = false;
  @Output() selectionChangedEvent = new EventEmitter();
  @Output() deleteEvent = new EventEmitter();
  @Output() achatEvent = new EventEmitter();
  code;
  
  @Input() isAchatVisible = true;

  get link() {
    if ( this.item && this.item.produit ) {
      return '/produits/' + this.item.produit.nom.replace( / /g , '-' );
    }
  }

  @Input() public set item(it) {

    this._item = it;
    /* code */
    if (it && it.variation) {
      this.code = it.variation.code;
    } else if ( it && it.produit ) {
      this.code = it.produit.code;
      
    }
    /* qte */
    if ( it && it.panier) {
      this.qte = it.panier.qte;
    }
    /* prix */
    if ( it && it.produit ) {
      if (it.produit.isPromo ) {
        this.produitPrix = it.produit.prixPromo;
      } else {
        this.produitPrix = it.produit.prixUnite;
      }
      this._item.prix = this.prix = this.qte * this.produitPrix;
    }
    this._item.panier.code = this.code;
  }
  
  public get item() {
    return this._item;
  }

  public get notEmptyQte() {
    return this.item.produit && this.item.produit.qte >= this.minQte || 
    this.item.variation && this.item.variation.qte >= this.minQte ;
  }
  public set selected (slt) {
    if ( !this.notEmptyQte ) { return; }
    this._selected = slt;
    this.selectionChangedEvent.emit();
  }

  @Output() public get selected() {
    return this._selected;
  }

  constructor() { }

  ngOnInit() {
  }

  quantiteChanged($event) {
    this.qte = $event;
    this.item.panier.qte = this.qte;
    this._item.prix = this.prix = this.qte * this.produitPrix;
    if (this.selected) {
      this.selectionChangedEvent.emit();
    }
    if ( !this.isAchatVisible) {
      this.selectionChangedEvent.emit();
    }
  }
  itemClicked() {
    this.selected = ! this.selected;
  }
  acheterClick($event) {
    this.achatEvent.emit(this.item);
    $event.stopPropagation();
  }
  handleClick($event) {
    $event.stopPropagation();
  }
  deleteClicked($event) {
    this.deleteEvent.emit(this.code);
    $event.stopPropagation();
  }
}
