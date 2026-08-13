import { Injectable } from '@angular/core';
import { HttpEvent, HttpRequest, HttpResponse, HttpInterceptor, HttpHandler, HttpErrorResponse } from '@angular/common/http';
import { startWith, tap, catchError } from 'rxjs/operators';
import { Observable, throwError } from 'rxjs';
import { of } from 'rxjs';
import { RequeteCache } from '../services/requete-cache.service';
import { ErrorMessage } from '../data/ErrorMessage';

@Injectable()
export class CacheInterceptor implements HttpInterceptor {



  constructor(private cache: RequeteCache) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    
    const cachedResponse = this.cache.get(req);
    /*
    if (cachedResponse) {
      console.log('+++++ cached ' + req.urlWithParams);
    } else {
      
      console.log('----- caching ' + req.urlWithParams);
    }
    */
    return cachedResponse ? of(cachedResponse) : this.sendRequest(req, next, this.cache);
  }

  sendRequest(
    req: HttpRequest<any>,
    next: HttpHandler,
    cache: RequeteCache): Observable<HttpEvent<any>> {
    
    return next.handle(req).pipe(
      tap(event => {
        if (event instanceof HttpResponse) {
          cache.put(req, event);
        }
      }), catchError( this.handleError)
    );
  }
  
  handleError(error: HttpErrorResponse) {
    
    let errorMessage = '';
    /*
    if (error.error instanceof ErrorEvent) {
      // client-side error
    } else {
      // server-side error
      errorMessage = `Error Code: ${error.status}
      //\nMessage: ${error}`;
    }
    */
   if ( error && error.status) {
      errorMessage = '' + error.status;
   }
    return throwError(errorMessage);
  }

}
