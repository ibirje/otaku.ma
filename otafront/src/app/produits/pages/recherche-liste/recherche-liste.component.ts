import { Title } from '@angular/platform-browser';
import { HttpUrlEncodingCodec } from '@angular/common/http';
import { Const } from './../../../shared/const-common';
import { filter } from 'rxjs/operators';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { ProduitsService } from 'src/app/services/produits.service';
import { Produit } from 'src/app/data/Produit';

@Component({
  selector: 'app-recherche-liste',
  templateUrl: './recherche-liste.component.html',
  styleUrls: ['./recherche-liste.component.css']
})
export class RechercheListeComponent implements OnInit {


  produits: Produit[] ;
  size: number;
  page: number;
  prixmin: number;
  prixmax: number;
  tri: string;
  loading;
  listeVideText;
  codec = new HttpUrlEncodingCodec();
  

  constructor( 
    private service: ProduitsService, 
    private route: ActivatedRoute, 
    private router: Router , private titleService: Title) { 
  
      this.titleService.setTitle( 'Plein de goodies et accessoires de jeux video et de mangas sur Otaku.ma' );
    router.events.pipe(filter(event => event instanceof NavigationEnd)).subscribe( (val) => {

      this.loading = true; 
      this.produits = [];
      this.page = 1;
      this.prixmin = this.prixmax = null;
      
      const text =  this.codec.decodeKey(this.route.snapshot.url.toString().replace(/-/g, ' ')).toString().split(',')[1];

      if (text && text.length >= Const.MIN_LENGTH_RECHERCHE ) {

        this.service.countByText(null, null, text).subscribe (   (data: number) => { 
          this.size = data; 
          if ( this.produits ) { this.loading = false; }
        } 
          );
        this.service.produitsByText(null, null, null, null , text).subscribe( (data: Produit[] )    => { 
          this.produits = data;
          if ( this.size ) { this.loading = false; } 
          if (!this.produits || this.produits.length < 1) {
            this.listeVideText = 'Aucun resultat trouvé pour \'' + text + '\'' ;
          }
        });
      } else {
        this.loading = false;
        this.listeVideText = 'La phrase \'' + text + '\' est trop courte. réessayez avec un mot plus long';
      }
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

    const text =  this.codec.decodeKey(this.route.snapshot.url.toString().replace(/-/g, ' ')).toString().split(',')[1];

    if (text && text.length >= Const.MIN_LENGTH_RECHERCHE ) {
      this.service.produitsByText(page, this.prixmin, this.prixmax, this.tri, text)
      .subscribe( (data: Produit[]  )    => { 
        this.produits = data; 
        this.loading = false;

         if (!this.produits || this.produits.length < 1) {
          this.listeVideText = 'Aucun resultat trouvé pour \'' + text + '\'' ;
        }
      });
    } else {
      this.loading = false;
      this.listeVideText = 'La phrase \'' + text + '\' est trop courte. réessayez avec un mot plus long';
    }
  }
  
  refreshProduits() {
    this.page = 1;
    const text =  this.codec.decodeKey(this.route.snapshot.url.toString().replace(/-/g, ' ')).toString().split(',')[1];
    
    this.loading = true;
    
    if (text && text.length >= Const.MIN_LENGTH_RECHERCHE ) {
      this.service.countByText(this.prixmin, this.prixmax, text).subscribe (   (data: {}) => { 
        this.size = data > 0 ? data as number : 0 ;
      });
      this.service.produitsByText(null, this.prixmin, this.prixmax, this.tri, text)
      .subscribe( (data: Produit[]  )    => { 
        this.produits = data; 
        this.loading = false;

        if (!this.produits || this.produits.length < 1) {
          this.listeVideText = 'Aucun resultat trouvé pour \'' + text + '\'' ;
        }
      });
    }  else {
      this.loading = false;
      this.listeVideText = 'La phrase \'' + text + '\' est trop courte. réessayez avec un mot plus long';
    }
  }
}
