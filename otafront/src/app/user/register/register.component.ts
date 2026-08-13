import { Title } from '@angular/platform-browser';
import { HttpErrorResponse } from '@angular/common/http';
import { AlertService } from './../../services/alert.service';
import { AuthenticationService } from './../../services/authentication.service';
import { FormGroup, FormBuilder, Validators, FormControl, FormGroupDirective, NgForm } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UtilisateurService } from 'src/app/services/utilisateur.service';
import { first } from 'rxjs/operators';
import { ErrorStateMatcher } from '@angular/material';

export class MyErrorStateMatcher implements ErrorStateMatcher {
    isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
        const invalidCtrl = !!(control && control.invalid && control.parent.dirty);
        const invalidParent = !!(control && control.parent && control.parent.invalid && control.parent.dirty);
    
        return (invalidCtrl || invalidParent);
    }
  }

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerForm: FormGroup;
  loading = false;
  submitted = false;
  success = false;

  constructor(
      private formBuilder: FormBuilder,
      private router: Router,
      private authenticationService: AuthenticationService,
      private userService: UtilisateurService, private titleService: Title) { 
        this.titleService.setTitle( 'Inscription' );
        
      if (this.authenticationService.currentUserValue) { 
          this.router.navigate(['/']);
      }
  }

  ngOnInit() {
      this.registerForm = this.formBuilder.group( {
          pseudo: ['', [Validators.required, Validators.email]],
          motdepasse: ['', [Validators.required, Validators.minLength(6)]],
          confirmpassword: ['', [Validators.required, Validators.minLength(6)]]
      }, {validator: this.checkPasswords}); // 
  }

  checkPasswords(group: FormGroup) { 
  const pass = group.controls.motdepasse.value;
  const confirmPass = group.controls.confirmpassword.value;
  return pass === confirmPass ? null : { notSame: true };
} // group.controls.confirmpassword.setErrors({ NoPassswordMatch: pass !== confirmPass });

  get f() { return this.registerForm.controls; }

    onSubmit() {
      this.submitted = true;
        
      if (this.registerForm.invalid) {
        return;
      }

      this.loading = true;
      this.userService.register({email: this.registerForm.value.pseudo, motdepasse : this.registerForm.value.motdepasse})
          .pipe(first())
          .subscribe(
              data => {
                this.success = true;
              },
              error => {
                const str = error as string;
                if ( str.toLowerCase() === 'found') {
                    this.registerForm.controls.pseudo.setErrors({ usedMail: true });
                }
                this.loading = false;
              });
    }
}
