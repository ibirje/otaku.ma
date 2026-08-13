import { RequeteCache } from './../services/requete-cache.service';
import { AuthenticationService } from './../services/authentication.service';
import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    constructor(private authenticationService: AuthenticationService) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // add authorization header with jwt token if available
        const currentUser = this.authenticationService.currentUserValue;

        if (request.method !== 'POST' && RequeteCache.exceptions.indexOf(request.urlWithParams) === -1 
        && RequeteCache.exceptionsNoParam.indexOf(request.url.split('?')[0]) === -1) {
            return next.handle(request);
        }
        if ( (request.method === 'POST' ||  request.method === 'PUT') && 
        RequeteCache.postWithNoLogin.indexOf(request.urlWithParams) !== -1) {
            return next.handle(request);
        } 

        if (currentUser && currentUser.token) {
            request = request.clone({
                setHeaders: { 
                    Authorization: `Bearer ${currentUser.token}`
                }
            });
        }

        return next.handle(request);
    }
}
