import { Title } from '@angular/platform-browser';
import { AlertService } from '../../services/alert.service';


import { AuthenticationService } from '../../services/authentication.service';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';

@Component({
    selector: 'app-login',
    templateUrl: 'login.component.html',
    styleUrls : ['login.component.css']
    })
export class LoginComponent implements OnInit {
    loginForm: FormGroup;
    loading = false;
    submitted = false;
    returnUrl: string;

    constructor( private formBuilder: FormBuilder, private route: ActivatedRoute,
        private router: Router, private authenticationService: AuthenticationService, private titleService: Title) { 
        this.titleService.setTitle( 'Connection' );
        // redirect to home if already logged in
        if (this.authenticationService.currentUserValue) { 
            this.router.navigate(['/']);
        }
    }

    ngOnInit() {
        this.loginForm = this.formBuilder.group({
            username: ['', [Validators.required, Validators.email]],
            password: ['', Validators.required]
        });

        // get return url from route parameters or default to '/'
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    }

    // convenience getter for easy access to form fields
    get f() { return this.loginForm.controls; }

    onSubmit() {
        this.submitted = true;

        // stop here if form is invalid
        if (this.loginForm.invalid) {
            return;
        }

        this.loading = true;
        this.authenticationService.login(this.f.username.value, this.f.password.value)
        .pipe(first())
        .subscribe( 
            data => { this.router.navigate([this.returnUrl]); },
            error => {
                const str = error as string;
                // console.log(str);
                
                if ( str === 'not acceptable') {
                    this.loginForm.controls.password.setErrors({ notFound: true });
                } else if ( str === 'found') {
                    this.loginForm.controls.username.setErrors({ mailNonConfirme: true });
                } else if ( str === 'not found') {
                    this.loginForm.controls.username.setErrors({ nonInscrit: true });
                }
                    this.loading = false;
            }
        );
    }
}
