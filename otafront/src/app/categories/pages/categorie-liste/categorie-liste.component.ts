import { HttpUrlEncodingCodec } from '@angular/common/http';
import { Title } from '@angular/platform-browser';
import { Categorie } from './../../../data/Categorie';
import { CategoriesService } from './../../../services/categories.service';
import { Const } from './../../../shared/const-common';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { ProduitsService } from 'src/app/services/produits.service';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-categorie-liste',
  templateUrl: './categorie-liste.component.html',
  styleUrls: ['./categorie-liste.component.css']
})
export class CategorieListeComponent implements OnInit {

 
  produits: {};
  size: number;
  page: number;
  prixmin: number;
  prixmax: number;
  tri: string;
  loading;
  
  related: {parent: Categorie, childs: Categorie[]}[];
  branche: Categorie[];

  constructor( 
    private service: ProduitsService, 
    private catservice: CategoriesService, 
    private route: ActivatedRoute, 
    private router: Router , private titleService: Title) { 

    this.setupCategories();
  
    router.events.pipe(filter(event => event instanceof NavigationEnd)).subscribe( (val) => {

      this.loading = true; 
      this.produits = [];
      this.page = 1;
      this.prixmin = this.prixmax = null;

      this.setupCategories();
      const categ = this.route.snapshot.url.toString().replace(/-/g, ' ');
      
      this.titleService.setTitle( categ + ' et plein de goodies et accessoires de jeux video et de mangas sur Otaku.ma' ); 
      const text = this.route.snapshot.queryParams.text;

      this.service.countByCategorie(categ, null, null, text).subscribe (   (data: number) => { 
        this.size = data; 
        if ( this.produits ) { this.loading = false; } 
      } 
        );
      this.service.produitsByCategorie(categ, null, null, null, null , text).subscribe( (data: {} )    => { 
        this.produits = data;
        if ( this.size ) { this.loading = false; } 
      } );
      
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
    const page = $event;
    this.page = page;
    this.loading = true;

    const categ = this.route.snapshot.url.toString().replace(/-/g, ' ');
    const text = this.route.snapshot.queryParams.text;

    this.service.produitsByCategorie(categ, page, this.prixmin, this.prixmax, this.tri, text)
    .subscribe( (data: {} )    => { 
      this.produits = data; 
      this.loading = false;
    } );
  }
  
  refreshProduits() {
    this.page = 1;
    const categ = this.route.snapshot.url.toString().replace(/-/g, ' ');
    const text = this.route.snapshot.queryParams.text;
    
    this.loading = true;
    this.service.countByCategorie(categ, this.prixmin, this.prixmax, text).subscribe (   (data: {}) => { 
      this.size = data > 0 ? data as number : 0 ;
    });
    this.service.produitsByCategorie(categ, null, this.prixmin, this.prixmax, this.tri, text)
      .subscribe( (data: {} )    => { this.produits = data; 
        this.loading = false;
      } );
  }


  setupCategories() {
    
    const codec = new HttpUrlEncodingCodec();
    const categ = codec.decodeKey(this.route.snapshot.url.toString().replace(/-/g, ' '));
    if (categ) {
      this.catservice.categories.subscribe(data => {
        
        const categories = data as [Categorie];
        categories.sort((one, two) => (one.code > two.code ? 1 : -1));
        let thiscateg: Categorie;
        let code: string = null;
        categories.forEach(c => { if (c.nom === categ) { code = c.code; thiscateg = c; }});
        if (!code) { return; }
        this.branche = [];
        this.related = [];
        for (const catparent of categories) {
          if (catparent.code.length < 8 && catparent.code.startsWith(code.substr(0, 2)) 
          && (Const.SHOW_EMPTY_TYPES || catparent.activeQte > 0 )
            && (Const.SHOW_LIST_EMPTY_PRODUITS || catparent.nombreProduits > 0 ) ) {  
            const fils: Categorie[] = [];
            if ( catparent.code.length < 5) { 
              this.related.push({parent: catparent, childs: fils}); 
              this.branche.push(catparent);
              continue; 
            } else if (code.length > 4 && catparent.code.startsWith(code.substr(0, 5))) {
              this.branche.push(catparent);  
            }
            for (const cat of categories) {
              if ( cat.code.startsWith((catparent.code as string)) && !(cat.code === catparent.code) 
              &&  ( Const.SHOW_EMPTY_TYPES || cat.activeQte > 0) && (Const.SHOW_LIST_EMPTY_PRODUITS || cat.nombreProduits > 0 )) {
                fils.push(cat);
              }
            }
              this.related.push({parent: catparent, childs: fils});
          }
        }
        if ( thiscateg.code.length > 7 ) { this.branche.push(thiscateg); }
      });
    }
  }
}
