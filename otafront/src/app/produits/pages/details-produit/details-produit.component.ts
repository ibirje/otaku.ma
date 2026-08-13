import { SeoService } from './../../../services/seo.service';
import { Title } from '@angular/platform-browser';
import { FullAttribut } from '../../../data/restdata/FullAttribut';
import { Categorie } from '../../../data/Categorie';
import { FullVariation } from '../../../data/restdata/FullVariation';

import { RouterModule, Routes, ActivatedRoute } from '@angular/router';
import { FullProduit } from '../../../data/restdata/FullProduit';
import { ProduitsService } from '../../../services/produits.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Produit } from 'src/app/data/Produit';
import { Theme } from 'src/app/data/Theme';

@Component({
  selector: 'app-details-produit',
  templateUrl: './details-produit.component.html'
})
export class DetailsProduitComponent implements OnInit {

  @ViewChild('slideshow') slideshow: any;
  fullproduit: FullProduit;
  produit: Produit;
  theme: Theme;
  variations: [FullVariation];
  attributs: [FullAttribut];
  categories: [Categorie];
  
  constructor(private service: ProduitsService, private route: ActivatedRoute , private titleService: Title, private seo: SeoService) { 
  }

  ngOnInit() {
    this.service.fetchFullProduit(this.route.snapshot.url.toString().replace(/-/g , ' ')).subscribe((data: FullProduit) => {
      // console.log(data);
      this.fullproduit = data;
      this.produit = this.fullproduit.produit;
      this.variations = this.fullproduit.variations;
      this.categories = this.fullproduit.categories;
      this.theme = this.fullproduit.theme;
      this.attributs = this.fullproduit.attributs;
  
      this.titleService.setTitle( this.produit.nom + ' dans ' + this.categories[0].nom +
      ' et plein de goodies et accessoires de jeux video et de mangas sur Otaku.ma' ); 

      this.seo.updateOG('image', this.produit.image1);
      this.seo.updateOG('image:image:secure_url', this.produit.image1);
      this.seo.updateOG('url', this.route.url);

    });
    
  }
  selectionChanged($event) {
    const selected = $event;
    if ( selected != null) {
      
      this.slideshow.selectSlideImg(selected);
      
    }
  }
/*
  randClick() {
    this.slideshow.selectSlideId(this.randnum);
  }
  
  nextClick() {
    
    this.slideshow.nextSlide();
  }
  
  lastClick() {
    
    this.slideshow.lastSlide();
  }
  */
}
