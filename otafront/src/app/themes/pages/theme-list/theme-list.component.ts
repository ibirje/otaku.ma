import { HttpUrlEncodingCodec } from '@angular/common/http';
import { Title } from '@angular/platform-browser';
import { Const } from './../../../shared/const-common';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { filter } from 'rxjs/operators';
import { ProduitsService } from 'src/app/services/produits.service';
import { ThemesService } from 'src/app/services/themes.service';
import { Theme } from 'src/app/data/Theme';

@Component({
  selector: 'app-theme-list',
  templateUrl: './theme-list.component.html',
  styleUrls: ['./theme-list.component.css']
})
export class ThemeListComponent implements OnInit {

  produits: {};
  size: number;
  page: number;
  prixmin: number;
  prixmax: number;
  tri: string;
  
  related: {parent: Theme, childs: Theme[]}[];
  branche: Theme[];
  
  loading ;


  constructor( 
    private service: ProduitsService, 
    private route: ActivatedRoute, 
    private themesservice: ThemesService, 
    private router: Router, private titleService: Title) {


    this.refreshThemes();

    router.events.pipe(filter(event => event instanceof NavigationEnd)).subscribe( (val) => {
      
      this.loading = true; 
      this.produits = [];
      this.page = 1;
      this.prixmin = this.prixmax = null;

      this.refreshThemes();

      const theme = this.route.snapshot.url.toString().replace(/-/g, ' ');
      
      this.titleService.setTitle( theme + ' et plein de goodies et accessoires de jeux video et de mangas sur Otaku.ma' );
      
      const text = this.route.snapshot.queryParams.text;

      this.service.countByTheme(theme, null, null, text).subscribe (   (data: number) => { 
        this.size = data; 
        if ( this.produits ) { 
          this.loading = false; 
        } 
      } );
      this.service.produitsByTheme(theme, null, null, null, null, text).subscribe( (data: {} ) => { 
        this.produits = data; 
        // console.log(data);
        if ( this.size ) { 
          this.loading = false; 
        } } );
      
    });
  }

  ngOnInit() {

  }

  triChanged($event) {
    this.tri = $event;
    this.tri = this.tri === 'pertinence' ? null : this.tri;
    this.refreshProduits();
  }
  prixChanged($event) {
    const prixrange = $event;
    this.prixmin = prixrange[0];
    this.prixmax = prixrange[1];
    this.prixmin = this.prixmin === 0 ? null : this.prixmin;
    this.prixmax = this.prixmax >= Const.FILTRE_PRIX_MAX ? null : this.prixmax;
    // console.log(this.prixmin + ' ' + this.prixmax);
    this.refreshProduits();
  }

  pageChangedEvent($event) {
    this.loading = true; 
    const page = $event;
    this.page = page;
    const theme = this.route.snapshot.url.toString().replace(/-/g, ' ');
    const text = this.route.snapshot.queryParams.text;
    this.service.produitsByTheme(theme, page, this.prixmin, this.prixmax, this.tri, text)
      .subscribe( (data: {} )    => { this.produits = data; 
        this.loading = false; } );
  }
  
  refreshProduits() {
    this.page = 1;
    this.loading = true;  
    const theme = this.route.snapshot.url.toString().replace(/-/g, ' ');
    const text = this.route.snapshot.queryParams.text;
    this.service.countByTheme(theme, this.prixmin, this.prixmax, text).subscribe (   (data: {}) => { 
      this.size = data > 0 ? data as number : 0 ; 
      this.loading = false; 
      // console.log(this.size);
    });
    this.service.produitsByTheme(theme, null, this.prixmin, this.prixmax, this.tri, text)
      .subscribe( (data: {} )    => { 
        this.produits = data;
        this.loading = false;  
      } );
  }
  refreshThemes() {
    
    const codec = new HttpUrlEncodingCodec();
    const theme = codec.decodeKey(this.route.snapshot.url.toString().replace(/-/g, ' '));
    
    this.themesservice.themes.subscribe((data: {}) => {

      const themes = data as Theme[];
      if ( !themes || themes.length < 1) {
        return;
      }
      // console.log(themes);
      
      themes.sort((one, two) => (one.code > two.code ? 1 : -1));
      
      let thistheme: Theme;
      let code: string = null;
      
      themes.forEach(c => { if (c.nom === theme) { code = c.code; thistheme = c; }} );
      // console.log(thistheme);
      if (!code || !thistheme) {
        return;
      }
      this.branche = [];
      this.related = [];
      for (const thparent of themes) {
        
        if (thparent.code.length === 2 && code.startsWith(thparent.code) && ( Const.SHOW_EMPTY_TYPES || thparent.activeQte > 0)
        && (Const.SHOW_LIST_EMPTY_PRODUITS || thparent.nombreProduits > 0 )) {
          this.branche.push(thparent);
          const fils: Theme[] = [];
          
          for (const cat of themes) {
            if ( cat.code.startsWith((thparent.code as string)) &&  ! (cat.code === thparent.code) 
            && ( Const.SHOW_EMPTY_TYPES || cat.activeQte > 0) && (Const.SHOW_LIST_EMPTY_PRODUITS || cat.nombreProduits > 0 )) {
              fils.push(cat);
            }
          }
          this.related.push({parent: thparent, childs: fils} );
          if ( thistheme.code.length > 3 ) { this.branche.push(thistheme); }
          return;
        }
      }
    });
  }
}
