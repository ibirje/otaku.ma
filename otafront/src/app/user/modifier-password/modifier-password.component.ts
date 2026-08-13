import { Title } from '@angular/platform-browser';
import { first, filter } from 'rxjs/operators';
import { AuthenticationService } from './../../services/authentication.service';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { UtilisateurService } from 'src/app/services/utilisateur.service';

@Component({
  selector: 'app-modifier-password',
  templateUrl: './modifier-password.component.html',
  styleUrls: ['./modifier-password.component.css']
})
export class ModifierPasswordComponent implements OnInit {

  registerForm: FormGroup;
  loading = false;
  submitted = false;
  key;
  erreur = null;

  constructor( private route: ActivatedRoute, private formBuilder: FormBuilder,
    private router: Router, private authenticationService: AuthenticationService,
    private userService: UtilisateurService, private titleService: Title) { 
      this.titleService.setTitle( 'Modifier votre mot de passe' );
      
    if (this.authenticationService.currentUserValue) { 
        this.router.navigate(['/']);
    }
    
    router.events.pipe(filter(event => event instanceof NavigationEnd)).subscribe( (val) => {
      
      this.key = this.route.snapshot.url.toString().substring('modifiermotdepasse,'.length);
      if ( this.key == null) {
        this.erreur = 'invalide';
      } else {
        this.userService.isRecupCodeValide(this.key) .pipe(first())
        .subscribe(data => { },
        error => { this.erreur = 'expire'; this.loading = false; } );
      }
    
    });
    
}

  ngOnInit() {
    this.registerForm = this.formBuilder.group( {
        motdepasse: ['', [Validators.required, Validators.minLength(6)]],
        confirmpassword: ['', [Validators.required, Validators.minLength(6)]]
    }, {validator: this.checkPasswords});
  }

  checkPasswords(group: FormGroup) { 
    const pass = group.controls.motdepasse.value;
    const confirmPass = group.controls.confirmpassword.value;
    return pass === confirmPass ? null : { notSame: true };
  }

  get f() { return this.registerForm.controls; }

  onSubmit() {
    this.submitted = true;
      
    if (this.registerForm.invalid) {
      return;
    }
    const key = this.key;
    this.loading = true;
    this.userService.recupMotDePasse(this.registerForm.value.motdepasse, key)
      .pipe(first())
      .subscribe(
        data => {
          this.router.navigate(['/user/login']);
        },
        error => {
          const str = error as string;
          if ( str.toLowerCase() === 'not found') {
            this.registerForm.controls.pseudo.setErrors({ notfound: true });
          }
          this.loading = false;
        }
      );
  }
}
