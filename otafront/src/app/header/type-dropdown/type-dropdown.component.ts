import { Const } from './../../shared/const-common';
import { CategoriesService } from './../../services/categories.service';
import { Categorie } from './../../data/Categorie';
import { Component, OnInit, Input } from '@angular/core';
import { Theme } from 'src/app/data/Theme';
import { ThemesService } from 'src/app/services/themes.service';

@Component({
  selector: 'app-type-dropdown',
  templateUrl: './type-dropdown.component.html',
  styleUrls: ['./type-dropdown.component.css']
})
export class TypeDropdownComponent implements OnInit {

  categories: Categorie[];
  sortedcategories: {parent: Categorie, childs: Categorie[]}[];
  sortedthemes: {parent: Theme, childs: Theme[]}[];
  themes: Theme[];

  @Input() set  type(tp: string) {
    
    if (tp != null && tp === 'categorie') {

      this.categservice.categories.subscribe((data: {}) => {

        this.categories = data as [Categorie];
        this.categories.sort((one, two) => (one.code > two.code ? 1 : -1));
        this.sortedcategories = [];
        for (const catparent of this.categories) {
          if (catparent.code.length < 8 && (Const.SHOW_EMPTY_TYPES || catparent.activeQte > 0 )
            && (Const.SHOW_LIST_EMPTY_PRODUITS || catparent.nombreProduits > 0 ) ) {  
  
            const fils: Categorie[] = [];
            if ( catparent.code.length < 5) { this.sortedcategories.push({parent: catparent, childs: fils}); continue; }
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
    } else {
      this.themeservice.themes.subscribe((data: {}) => {

        this.themes = data as Theme[];
        if ( !this.themes || this.themes.length < 1) {
          return;
        }
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
  constructor(private categservice: CategoriesService, private themeservice: ThemesService) { }


  ngOnInit() {
    
  }

}
