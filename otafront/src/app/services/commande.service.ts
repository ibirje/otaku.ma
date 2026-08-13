import { BehaviorSubject } from 'rxjs';
import { ServerSettings } from './const-server';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class CommandeService {
    
    BASE_URL = ServerSettings.BASE_URL + 'commandes/';

    private commande = new BehaviorSubject(null);
    currentCommande = this.commande.asObservable();
  
    changeCommande(items: any) { 
        this.commande = new BehaviorSubject(null);
        this.currentCommande = this.commande.asObservable();
        this.commande.next(items);
    }

    constructor(private http: HttpClient) { }
    
    passerCommande(cmd: any) {
        return this.http.post(this.BASE_URL + 'passercommande/', cmd);
    }
    annulerCommande(code: any) {
        return this.http.post(this.BASE_URL + 'annulercommande', code);
    }
    getCommandes(option?: any, code?: string, page?: number) { // TODO filtres
        
        option = option ? option : 'commandes';
        let params = '?option=' + option;
        if ( code ) { params += '&code=' + code; }
        if ( page && page > 1 ) { params += '&page=' + page; }

        return this.http.get(this.BASE_URL + 'getcommandes' + params);
    }
}
