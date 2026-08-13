import { AuthenticationService } from './../services/authentication.service';
import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { HttpUrlEncodingCodec } from '@angular/common/http';


@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
    constructor( private router: Router, private authenticationService: AuthenticationService ) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const currentUser = this.authenticationService.currentUserValue;
        if (currentUser) {
            // authorised so return true
            return true;
        }

        // not logged in so redirect to login page with the return url
        const codec = new HttpUrlEncodingCodec();
        this.router.navigate(['/user/login'], { queryParams: { returnUrl: codec.decodeKey(state.url) }});
        return false;
    }
}
