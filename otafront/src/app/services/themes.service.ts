import { ServerSettings } from './const-server';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, of, BehaviorSubject } from 'rxjs';
import { map, catchError, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ThemesService {
  
  BASE_URL = ServerSettings.BASE_URL + 'type/';

   httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/x-www-form-urlencoded'
    })
  };

  
  private _themesSubject;
  private currentThemes;

  initSubject(items: any) { 
      this._themesSubject = new BehaviorSubject(null);
      this.currentThemes = this._themesSubject.asObservable();
      this._themesSubject.next(items);
  }

  constructor(private http: HttpClient) {
  }

  private extractData(res: Response) {
    return res || { };
  }

  private fetchThemes(): Observable<any> {
    return this.http.get(this.BASE_URL + 'themelist').pipe(
      map(this.extractData));
  }
  
  get themes() {
    /*
    if (!this.currentThemes) {
      this.fetchThemes().subscribe(it => { this.initSubject(it); });
    }
    return this.currentThemes;
    */
    return this.fetchThemes();
  }
}
