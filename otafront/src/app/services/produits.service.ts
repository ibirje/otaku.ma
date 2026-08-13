import { Const } from './../shared/const-common';
import { ServerSettings } from './const-server';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { PanierItem } from '../data/PanierItem';
@Injectable({
  providedIn: 'root'
})

export class ProduitsService {
  
  BASE_URL = ServerSettings.BASE_URL + 'produits/';

   httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
  };
  
  constructor(private http: HttpClient) {
  }
  
  private extractData(res: Response) {
    return res || { };
  }
  /* produit liste utilisée dans categ/theme list, recherche liste */
  private fetchProduits(params: string): Observable<any> {
    const url = params == null ? 'list' : 'list' + params;
    return this.http.get(this.BASE_URL + url).pipe(
      map(this.extractData));
  }

  /* nombre produits liste utilisée dans la pagination de categ/theme list, recherche liste */
  private countProduits(params: string): Observable<any> {
    const url = params == null ? 'listcount' : 'listcount' + params;
    return this.http.get(this.BASE_URL + url).pipe(
      map(this.extractData));
  }

  /* utilisée dans detailsproduit */
  public fetchFullProduit(nom: String): Observable<any> {
    return this.http.get(this.BASE_URL + 'fullproduit?nom=' + nom);
  }
  
  produitsByCategorie(categorie: string, page: number, prixmin: number, prixmax: number, tri: string, text: string) {
    /*
    ADD FILTRE TEXT TO RECHERCHE THEME / CATEGORIE
    add filtre to url ? /categories/nom?text= ????
    */
    let _produits: Observable<any>;
    let params = '?categorie=' + categorie.trim().replace(/\s/g, '-' );
    params += page == null || page === 1 ? '' : '&page=' + page ;
    params += prixmin == null ? '' : '&prixmin=' + prixmin ;
    params += prixmax == null ? '' : '&prixmax=' + prixmax ;
    params += tri == null ? '' : '&trifiltre=' + tri ;
    params += text == null || text.length < Const.MIN_LENGTH_RECHERCHE ? '' : '&nom=' + text;
    _produits = this.fetchProduits(params);
    return _produits;
  }
  countByCategorie(categorie: string, prixmin: number, prixmax: number, text: string) {
    
    let count: Observable<any>;
    let params = '?categorie=' + categorie.trim().replace(/\s/g, '-' );
    params += prixmin == null ? '' : '&prixmin=' + prixmin ;
    params += prixmax == null ? '' : '&prixmax=' + prixmax ;
    params += text == null || text.length < Const.MIN_LENGTH_RECHERCHE ? '' : '&nom=' + text;
    count = this.countProduits(params);
    return count;
  }

  produitsByTheme(theme: string, page: number, prixmin: number, prixmax: number, tri: string, text: string) {
    let _produits: Observable<any>;
    let params = '?theme=' + theme.trim().replace(/\s/g, '-' );
    params += page == null || page === 1 ? '' : '&page=' + page ;
    params += prixmin == null ? '' : '&prixmin=' + prixmin ;
    params += prixmax == null ? '' : '&prixmax=' + prixmax ;
    params += tri == null ? '' : '&trifiltre=' + tri ;
    params += text == null || text.length < Const.MIN_LENGTH_RECHERCHE ? '' : '&nom=' + text;
    _produits = this.fetchProduits(params);
    return _produits;
  }
  countByTheme(theme: string, prixmin: number, prixmax: number, text: string) {
    let _produits: Observable<any>;
    let params = '?theme=' + theme.trim().replace(/\s/g, '-' );
    params += prixmin == null ? '' : '&prixmin=' + prixmin ;
    params += prixmax == null ? '' : '&prixmax=' + prixmax ;
    params += text == null || text.length < Const.MIN_LENGTH_RECHERCHE ? '' : '&nom=' + text;
    
    _produits = this.countProduits(params);
    return _produits;
  }
  
  produitsByText( page: number, prixmin: number, prixmax: number, tri: string, text: string) {
    let _produits: Observable<any>;
    let params = '?nom=' + text;
    params += page == null || page === 1 ? '' : '&page=' + page ;
    params += prixmin == null ? '' : '&prixmin=' + prixmin ;
    params += prixmax == null ? '' : '&prixmax=' + prixmax ;
    params += tri == null ? '' : '&trifiltre=' + tri ;
    
    _produits = this.fetchProduits(params);
    return _produits;
  }
  countByText( prixmin: number, prixmax: number, text: string) {
    let _produits: Observable<any>;
    let params = '?nom=' + text;
    params += prixmin == null ? '' : '&prixmin=' + prixmin ;
    params += prixmax == null ? '' : '&prixmax=' + prixmax ;
    
    _produits = this.countProduits(params);
    return _produits;
  }

  getHomePromos() {
    return this.http.get(this.BASE_URL + 'homeproduits');
  }
  getPanier() {
    return this.http.get(this.BASE_URL + 'getpanier');
  }
  addPanier(item: PanierItem) {
    return this.http.post(this.BASE_URL + 'ajoutpanier', item, this.httpOptions );
  }
  deletePanier(item: any) { 
    return this.http.post(this.BASE_URL + 'deletepanier', item, this.httpOptions );
  }
  getSuggestions(text: string) {
    return this.http.get(this.BASE_URL + 'suggestions?text=' + text);
  }
}

