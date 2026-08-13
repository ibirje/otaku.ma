import { Title } from '@angular/platform-browser';
import { Livraison } from './../../data/Livraison';
import { CommandeService } from './../../services/commande.service';
import { Adresse } from './../../data/adresse';
import { SelectAdresseComponent } from './select-adresse/select-adresse.component';
import { FullPanier } from './../../data/restdata/FullPanier';
import { ItemComponent } from './../panier/item/item.component';
import { Router } from '@angular/router';
import { PanierItem } from './../../data/PanierItem';
import { Component, OnInit, ViewChildren, QueryList, AfterViewInit, ViewChild } from '@angular/core';


@Component({
  selector: 'app-passer-commande',
  templateUrl: './passer-commande.component.html',
  styleUrls: ['./passer-commande.component.css']
})
export class PasserCommandeComponent implements OnInit {
  
  @ViewChildren(ItemComponent) itemcomps: QueryList<ItemComponent>;
  @ViewChild('selectadresse') selectadresse: SelectAdresseComponent;
  _isVB = false;
  isLoadingProduits = true;
  items: FullPanier[];
  
  _selectedCoursier;
  err;

  quantiteTotale = 0;
  prixPieces     = 0;
  prixLivraison  = 0;
  prixTotal      = 0;

  coursiers: Livraison[] = [{
    coursier: 'Amana', prixPL_LD: 50, coutPL_LD: 56, prixVB_LD: 45, coutVB_LD: 56, 
    prixPL_PR: 50, coutPL_PR: 50, prixVB_PR: 45, coutVB_PR: 50, ville: 'any', codes: ['any']
  }];

  set selectedCoursier(str: string) {
    if (!str) { return; }
    this._selectedCoursier = str;
    this.calculerPrixLivraison();
  }
  get selectedCoursier() {
    return this._selectedCoursier;
  }
  set isVB(vb) { this._isVB = vb; this.calculerPrixLivraison(); }
  get isVB() { return this._isVB; }
  /*
  acronymes :
  Livraison à domicile (LD)
  Point de relais (PR)
  Virement banquaire (VB)
  Paiement à la livraison (PL)

  *****************************************
  ** ADD commande.livraison in http.post **
  *****************************************

  */

  calculerPrixLivraison() {
    const      str = this._selectedCoursier;
    const coursier = str.substr( 0, str.length - 3 );
    const    mtliv = str.substr(str.length - 2, 2);
    
    this.coursiers.forEach(cr => {
      if (cr.coursier === coursier) { 
        this.prixLivraison = this.isVB && mtliv === 'LD' ? cr.prixVB_LD :  this.isVB ? cr.prixVB_PR : 
        !this.isVB && mtliv === 'LD' ? cr.prixPL_LD : cr.prixPL_PR;
        return; 
      }
    });
    if ( !this.prixLivraison ) { this.selectedCoursier = 'Amana_LD'; }
    this.somme();
  }
  
  constructor(private router: Router, private cmdservice: CommandeService, private titleService: Title) { 

    this.titleService.setTitle( 'Passer commande' );

    this.cmdservice.currentCommande.subscribe(cmd => {
      this.items = cmd as FullPanier[];
      this.verifDonnees();
      this.selectedCoursier = 'Amana_LD';
    }, error => { this.verifDonnees(); });
  }
  get hasNoItems() { 
    return !this.isLoadingProduits && !(this.items && this.items.length > 0); 
  }
  verifDonnees() {  
    this.isLoadingProduits = false; 
    if (this.hasNoItems) { this.router.navigate(['/user/panier']); }
  }
  ngOnInit() {
  }

  somme() {
    this.quantiteTotale = 0;
    this.prixPieces      = 0;
    if (this.items) {
      this.items.forEach(item => {
        this.quantiteTotale += item.panier.qte;
        this.prixPieces += item.prix;
      });
    }
    this.prixTotal = this.prixPieces + this.prixLivraison;
  }

  onSelectionChanged() {
    if (!this.itemcomps) { return; }
    this.quantiteTotale = 0;
    this.prixPieces      = 0;

    this.itemcomps.forEach(item => {
        this.quantiteTotale += item.qte;
        this.prixPieces += item.prix;
    });
    this.prixTotal = this.prixPieces + this.prixLivraison;
  }
  get codePostalCourant(): string {
    return this.adresseCourante ? this.adresseCourante.codePostal : 'any';
  }
  get adresseCourante(): Adresse {
    return this.selectadresse ? this.selectadresse.selectedAdresse as Adresse : null;
  }

  PasserCommande() {
    
    const addr: Adresse = this.adresseCourante;
    if (!addr || !addr.adresse1 || addr.adresse1 === '') {
      this.err = 'L\'adresse selectionnée est vide ou invalide';
      return;
    }
    const paiement = this.isVB  ? 'VB' : 'PAL';
    const livraison = this._selectedCoursier;

    const paniers: PanierItem[] = [];
    for (const it of this.items) { paniers.push(it.panier); }

    this.cmdservice.passerCommande({items : paniers, paiement : paiement, livraison : livraison})
    .subscribe(data => { this.router.navigate(['/user/commandes']); }, 
      error => { 
        if (error === 'not acceptable') {this.err = 'Quantité insuffisante'; }
        if (error === 'unauthorized') {this.err = 'Adresse incomplète.'; }
    });

    // packet commande :
    // 1_ items (code + qte)
    // 2_ adresse
    // 3_ type paiement + type livraison
    // todo sever receive cmd

  }
  onDelete($event) {

    if (!this.items || this.items.length < 1) { return; }
    this.isLoadingProduits = true;
    
    const code = $event;
    let deleteitem = null;
    let index = 0;
    
    for (const item of this.items) { if (item.panier.code === code) { deleteitem = item; break; } index++; }

    this.items = this.items.reverse().splice(index, 1);
    this.somme();
    this.isLoadingProduits = false;
  }
}
