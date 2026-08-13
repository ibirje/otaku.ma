import { AuthenticationService } from '../../services/authentication.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UtilisateurService } from 'src/app/services/utilisateur.service';
import { first } from 'rxjs/operators';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-send-recupere-login',
  templateUrl: './send-recupere-login.component.html',
  styleUrls: ['./send-recupere-login.component.css']
})
export class SendRecupereLoginComponent implements OnInit {

  registerForm: FormGroup;
  loading = false;
  submitted = false;
  recupEnvoyee = null;

  constructor( private formBuilder: FormBuilder, private router: Router,
    private titleService: Title, private userService: UtilisateurService ) {
      
    this.titleService.setTitle( 'Récuperation du mot de passe' );
  }

  ngOnInit() {
    
    this.registerForm = this.formBuilder.group({
      pseudo: ['', [Validators.required, Validators.email]]
    });
  }
  get f() { return this.registerForm.controls; }

  onSubmit() {
    this.submitted = true;
      
    // stop here if form is invalid
    if (this.registerForm.invalid) {
      return;
    }

    this.loading = true;
    this.recupEnvoyee = this.registerForm.value.pseudo;
    this.userService.recupLogin(this.registerForm.value.pseudo)
    .pipe(first())
    .subscribe(
      data => {
        this.recupEnvoyee = this.registerForm.value.pseudo;
        // console.log('success ' + this.recupEnvoyee);
        
        //  this.router.navigate(['/user/login']);
      },
      error => {
        const str = (error as string).toLowerCase();
        // if ( str !== 'ok') { this.recupEnvoyee = null; }
        if ( str === 'not found') { this.registerForm.controls.pseudo.setErrors({ notFoundMail: true }); }
        this.loading = false;
      }
    );
  }
}
