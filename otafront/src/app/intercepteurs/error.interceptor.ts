import { AuthenticationService } from './../services/authentication.service';
import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';


@Injectable()
export class ErrorInterceptor implements HttpInterceptor {


    no_reload: [string] = // requetes qui sont gerées manuellement et pas besoin de reload
    [
        '.*public/produits/ajoutpanier'
    ];


    constructor(private authenticationService: AuthenticationService) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(catchError(err => {
            if (err.status === 401) {
                // auto logout if 401 response returned from api
                this.authenticationService.logout();
                let reload = true;
                for (const str of this.no_reload) {
                    if (request.url.match(str)) {
                        reload = false;
                    }
                }
                if (reload) {
                    // location.reload(true);
                }
            }
            
            let error = err && err.error && err.error.message ? err.error.message :  
            err && err.statusText ? err.statusText : 
            err && err.error ? err.error : err;
            error = error === '417' ? 'expectation failed' :
                    error === '406' ? 'not acceptable' :
                    error === '404' ? 'not found' : 
                    error === '403' ? 'forbidden' :  
                    error === '405' ? 'method not allowed' : 
                    error === '401' ? 'unauthorized' : 
                    error === '302' ? 'found' : 'forbidden';
            return throwError(error);
        }));
    }
}
