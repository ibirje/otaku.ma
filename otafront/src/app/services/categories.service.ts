import { ServerSettings } from './const-server';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class CategoriesService {
  
    BASE_URL = ServerSettings.BASE_URL + 'type/';

   httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/x-www-form-urlencoded'
    })
  };

  constructor(private http: HttpClient) {
  }

  private extractData(res: Response) {
    return res || { };
  }

  private fetchCategories(): Observable<any> {
    return this.http.get(this.BASE_URL + 'categlist').pipe(
      map(this.extractData));
  }
  
  get categories() {
    return this.fetchCategories();
  }
}
