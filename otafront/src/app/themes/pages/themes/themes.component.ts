import { Title } from '@angular/platform-browser';
import { Const } from './../../../shared/const-common';
import { ThemesService } from './../../../services/themes.service';
import { Component, OnInit } from '@angular/core';
import { Theme } from 'src/app/data/Theme';

@Component({
  selector: 'app-themes',
  templateUrl: './themes.component.html',
  styleUrls: ['./themes.component.css']
})
export class ThemesComponent implements OnInit {

  themes: Theme[];
  sortedthemes: {parent: Theme, childs: Theme[]}[];
  
  constructor( private service: ThemesService , private titleService: Title) { 
    this.titleService.setTitle( 'Plein de goodies et accessoires de jeux video et de mangas sur Otaku.ma' );
    this.service.themes.subscribe( data => { 
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

  ngOnInit() { }

}
