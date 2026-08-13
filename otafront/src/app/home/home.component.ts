import { Const } from './../shared/const-common';
import { Categorie } from './../data/Categorie';
import { Title } from '@angular/platform-browser';
import { CategoriesService } from './../services/categories.service';
import { Component, OnInit } from '@angular/core';
import { ProduitsService } from '../services/produits.service';
import { ThemesService } from '../services/themes.service';
import { Theme } from '../data/Theme';
import { Produit } from '../data/Produit';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  loading;
  
  promos: Produit[];
  news: Produit[];
  visites: Produit[];

  categories: Categorie[];
  sortedcategories: {parent: Categorie, childs: Categorie[]}[];
  themes: Theme[];
  sortedthemes: {parent: Theme, childs: Theme[]}[];

  constructor( 
    private service: ProduitsService, 
    private catservice: CategoriesService, private titleService: Title, private themeservice: ThemesService) { 

      this.titleService.setTitle( 'Plein de goodies et accessoires de jeux video et de mangas sur Otaku.ma' ); 
      this.setupCategories();
      this.setupThemes();
      this.setupPromotions();
    }
  ngOnInit() {
  }

  setupPromotions() {
    this.service.getHomePromos().subscribe( (pr: {promos: any, news: any} ) => { 
      this.promos = pr.promos as Produit[]; 
      this.news = pr.news as Produit[]; 
    });
  }
  setupCategories() {
    
    this.catservice.categories.subscribe((data: {}) => {

      this.categories = data as [Categorie];
      this.categories.sort((one, two) => (one.code > two.code ? 1 : -1));
      this.sortedcategories = [];
      for (const catparent of this.categories) {
        
        if (catparent.code.length < 8 
          && (Const.SHOW_EMPTY_TYPES || catparent.activeQte > 0 )
          && (Const.SHOW_LIST_EMPTY_PRODUITS || catparent.nombreProduits > 0 ) ) {  

          const fils: Categorie[] = [];
          if ( catparent.code.length < 5) {
          this.sortedcategories.push({parent: catparent, childs: fils});
          continue;
          }
          for (const cat of this.categories) {
            if ( cat.code.startsWith((catparent.code as string)) &&  ! (cat.code === catparent.code) 
            &&  ( Const.SHOW_EMPTY_TYPES || cat.activeQte > 0) 
            && (Const.SHOW_LIST_EMPTY_PRODUITS || cat.nombreProduits > 0 )) {
              fils.push(cat);
            }
          }
          this.sortedcategories.push({parent: catparent, childs: fils});
        }
      }

    });
  }
  setupThemes() {
    this.themeservice.themes.subscribe( data => { 
      this.themes = data as Theme[];
      
      if ( !this.themes || this.themes.length < 1) { return; }
      // console.log(this.themes);
      
      this.themes.sort((one, two) => (one.code > two.code ? 1 : -1));
      this.sortedthemes = [];
      for (const thparent of this.themes) {
        
        if (thparent.code.length === 2 && ( Const.SHOW_EMPTY_TYPES || thparent.activeQte > 0)
        && (Const.SHOW_LIST_EMPTY_PRODUITS || thparent.nombreProduits > 0 )) {

          const fils: Theme[] = [];
          
          for (const cat of this.themes) {
            if ( cat.code.startsWith((thparent.code as string)) &&  ! (cat.code === thparent.code) 
            &&  ( Const.SHOW_EMPTY_TYPES || cat.activeQte > 0)
            && (Const.SHOW_LIST_EMPTY_PRODUITS || cat.nombreProduits > 0 )) {
              fils.push(cat);
            }
          }
          this.sortedthemes.push({parent: thparent, childs: fils});
        }
      }
    });
  }
}


