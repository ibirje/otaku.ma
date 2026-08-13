import { Router } from '@angular/router';
import { AuthenticationService } from './../../services/authentication.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-searchbar',
  templateUrl: './searchbar.component.html',
  styleUrls: ['./searchbar.component.css']
})
export class SearchbarComponent implements OnInit {

  text: string;

  constructor( private service: AuthenticationService,  private router: Router) { 

  }
  get user() { return this.service.currentUserValue; }

  ngOnInit() { }
  chercher(event) { if (event.key === 'Enter') { this.router.navigate(['/produits/recherche/' + this.text]); } }
}
