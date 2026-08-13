import { Component, OnInit, Input } from '@angular/core';

export class Categorie {

    public code: string;
    public keywords: string;
    public extra1: string;
    public extra2: string;
    public extra3: string;
    
    public link: string;
    
    @Input() public nom: string ;
    @Input() public description: string;
    @Input() public isActive: boolean;
    @Input() public smallImage: string;
    @Input() public mediumImage: string;
    @Input() public largeImage: string;
    
    public nombreProduits: number;
    public qte: number; 
    public activeQte: number;
    public pendingQte: number;

    constructor() {
    }
}
