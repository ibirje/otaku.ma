import { Title } from '@angular/platform-browser';
import { Const } from './../../../shared/const-common';
import { Categorie } from './../../../data/Categorie';
import { CategoriesService } from './../../../services/categories.service';
import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { ProduitsService } from 'src/app/services/produits.service';
import { Observable } from 'rxjs';
import { ThemesService } from 'src/app/services/themes.service';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent implements OnInit {

  categories: Categorie[];
  sortedcategories: {parent: Categorie, childs: Categorie[]}[];

  constructor(private categservice: CategoriesService, private titleService: Title) { 

    this.titleService.setTitle( 'Plein de goodies et accessoires de jeux video et de mangas sur Otaku.ma' ); 
    this.categservice.categories.subscribe((data: {}) => {

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

  ngOnInit() {
    
  }


}
