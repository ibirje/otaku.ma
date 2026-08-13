import { Title } from '@angular/platform-browser';
import { UtilisateurService } from './../../services/utilisateur.service';
import { first } from 'rxjs/operators';
import { AuthenticationService } from './../../services/authentication.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { loadInternal } from '@angular/core/src/render3/util';

@Component({
  selector: 'app-verifier-email',
  templateUrl: './verifier-email.component.html',
  styleUrls: ['./verifier-email.component.css']
})
export class VerifierEmailComponent implements OnInit {

  key: string;
  erreur: string;
  loading = false;

  constructor( private route: ActivatedRoute, private utilisateurService: UtilisateurService, 
    private router: Router, private titleService: Title) { 
    this.titleService.setTitle( 'Vérification de l\'adresse e-mail' );
    }

  ngOnInit() {
    this.key = this.route.snapshot.url.toString().substring('verifemail,'.length);
    // if key empty error null
    if ( this.key == null) {
      this.erreur = 'Lien invalide'; 
    } else {
      this.loading = true;
      this.utilisateurService.confirmEmail(this.key)
      .pipe(first())
      .subscribe(
          data => {
              this.router.navigate(['/']);
          },
          error => {
              const str = error as string;
              this.erreur = str;
              // console.log(str);
              /*
              if ( str.toLowerCase() === 'not acceptable') {

              }
              */
              this.loading = false;
          }
      );
    }
  }

}
