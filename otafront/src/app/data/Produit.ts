import { Component, OnInit, Input } from '@angular/core';

export class Produit {

    @Input() public code: string;
    @Input() public nom: string;
    @Input() keywords: string;
    @Input() public shortDescription: string;
    @Input() public description: string;
    @Input()  isActive: boolean;

    /* images */
    @Input() thumbnail: string;
    @Input() public image1: string;
    @Input() public image2: string;
    @Input() public image3: string;

    /* prix */
    _prixUnite: number;
    _prixPromo: number;
    public reduc;
    
    @Input() stringDateDebutPromo: string;
    @Input() stringDateFinPromo: string;

    dateDebutPromo: Date;
    dateFinPromo: Date;

    @Input()  _stars: number;
    @Input()  avis: number;
    @Input()  commandes: number;
    private _isPromo: boolean;
    @Input()  hasVariations: boolean;

    @Input()  categorie: string;
    @Input()  theme: string;
    @Input()  qte: number;
    
    @Input()  hasStars: boolean;

    
    constructor() {
    }

    @Input() public get isPromo() {
        return this._isPromo;
    }
    public set isPromo(pr) {
        this._isPromo = pr;
    }

    set stars( str ) {
        this._stars = str;
        this.avis = 0;
        this.commandes = 0;
        this.hasStars = this._stars > 0 ;
    }
    get stars () {
        return this._stars;
    }

    /************ prix unite **********/
    @Input() set prixUnite(prix: number) {
        this._prixUnite = prix;
    }
    
    get prixUnite() {
        return this._prixUnite;
    }

    /************ prix promo **********/
    @Input() set prixPromo(prix: number) {
        this._prixPromo = prix;
    }
    get prixPromo() {
        return this._prixPromo;
    }
    public get link() {
        return '/produits/' + this.nom.replace( / /g , '-' );
    }

}
