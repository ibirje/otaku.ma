import { BehaviorSubject } from 'rxjs';
import { ServerSettings } from './const-server';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class UtilisateurService {
    
    BASE_URL = ServerSettings.BASE_URL + 'authentication/';


    constructor(private http: HttpClient) { }

    saveAdresse(adresse: any) {
        return this.http.post(this.BASE_URL + 'saveadresse', adresse);
    }
    updateAdresse(adresses: any) {
        return this.http.post(this.BASE_URL + 'updateadresse', adresses);
    }
    selectAdresse(adresse: any) {
        return this.http.post(this.BASE_URL + 'selectadresse', adresse);
    }
    getAdresses() {
        return this.http.get(this.BASE_URL + 'getadresses');
    }
    register(user: any) {
        return this.http.post(this.BASE_URL + 'inscription', user);
    }
    update(user: any) {
        return this.http.post(this.BASE_URL + 'update/', user);
    }
    updateMotDePasse(user: any) {
        return this.http.post(this.BASE_URL + 'updatepassword/', user);
    }
    confirmEmail(key: String) {
        return this.http.post(this.BASE_URL + 'confirmEmail/' + key, {email : ''} );
    }
    recupLogin(email: String ) {
        return this.http.post(this.BASE_URL + 'recuperepassword/', {email : email});
    }
    delete(id: number) {
        return this.http.delete(this.BASE_URL + 'supprimer/' + id);
    }
    isRecupCodeValide(key: string) {
        return this.http.post(this.BASE_URL + 'validerecupcode/' + key, {email : ''} );
    }
    recupMotDePasse(pwd: string, key: string) {
        return this.http.post(this.BASE_URL + 'validerecupmotdepasse/', {motdepasse : pwd, recupkey : key} );
    }

}
