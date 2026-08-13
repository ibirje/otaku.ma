import { Route } from '@angular/compiler/src/core';
import { HttpUrlEncodingCodec } from '@angular/common/http';
import { AuthenticationService } from './../../services/authentication.service';
import { Component, OnInit } from '@angular/core';
import { Router} from '@angular/router';

@Component({
  selector: 'app-dropdown',
  templateUrl: './dropdown.component.html',
  styleUrls: ['./dropdown.component.css']
})
export class DropdownComponent implements OnInit {

  get user() {
    return this.service.currentUserValue;
  }
  get username() {
    const username = !this.user ? 'Indéfini' : 
      this.user.pseudo && this.user.pseudo.trim() !== '' ? this.user.pseudo :
      this.user.nom && this.user.nom.trim() !== '' ? this.user.nom + ' ' + this.user.prenom : this.user.email;
    
    return username;
  }
  logout() {
    this.service.logout();

    const routeConfig = this.router.config.find(f => f.path === this.router.url.toString().split('/')[1]);

    let conf = routeConfig;
    if (routeConfig && routeConfig.children) {
      let route_index = 2;
      while (conf && conf.children) {
        const children = (conf.children[0] as any)._loadedConfig.routes ;
        conf = (children.find(f => f.path === this.router.url.toString().split('/')[route_index]));
        route_index++;
      }
    }

    

    if (conf != null && conf.canActivate != null)  {
      const codec = new HttpUrlEncodingCodec();
      this.router.navigate(['/user/login'], { queryParams: { returnUrl: codec.decodeKey(this.router.url.toString()) }});
    }
  }

login() {
  const codec = new HttpUrlEncodingCodec();
  this.router.navigate(['/user/login'], { queryParams: { returnUrl: codec.decodeKey(this.router.url.toString()) }});
}

constructor( private router: Router, private service: AuthenticationService) { }

  ngOnInit() {
  }

}
