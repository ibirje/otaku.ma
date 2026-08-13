import { ServerSettings } from './const-server';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { map } from 'rxjs/operators';
import { Utilisateur } from '../data/Utilisateur';


@Injectable({ providedIn: 'root' })
export class AuthenticationService {

    httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };


    private currentUserSubject: BehaviorSubject<Utilisateur>;
    public currentUser: Observable<Utilisateur>;
    
    BASE_URL = ServerSettings.BASE_URL + 'authentication/';

    constructor(private http: HttpClient) {
        this.currentUserSubject = new BehaviorSubject<Utilisateur>(JSON.parse(localStorage.getItem('currentUser')));
        this.currentUser = this.currentUserSubject.asObservable();
    }

    public get currentUserValue(): Utilisateur {
        return this.currentUserSubject.value;
    }

    login(pseudo: string, motdepasse: string) {
        return this.http.post(this.BASE_URL + 'connect', { email : pseudo, motdepasse : motdepasse }, this.httpOptions)
        .pipe(map(user => {
            const utilisateur = user as Utilisateur;
            if (utilisateur && utilisateur.token) {
                // save user f login
                localStorage.setItem('currentUser', JSON.stringify(user));
                this.currentUserSubject.next(utilisateur);
            }
            return utilisateur;
        })); // , catchError(this.handleError)
    }

    handleError(error) {
        let errorMessage = '';
        if (error.error instanceof ErrorEvent) {
          // client error
          // errorMessage = `Error: ${error.error.message}`;
        } else {
          // server error
          errorMessage = `Error Code: ${error.status}`;
        }
        return throwError(errorMessage);
    }

    logout() {
        // remove user fe logout
        localStorage.removeItem('currentUser');
        this.currentUserSubject.next(null);
    }
}
