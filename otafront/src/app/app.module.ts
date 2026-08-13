import { HomeComponent } from './home/home.component';
import { SharedModule } from './shared/shared.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { ErrorInterceptor } from './intercepteurs/error.interceptor';
import { JwtInterceptor } from './intercepteurs/jwt.interceptor';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';

import { RequeteCache } from './services/requete-cache.service';
import { AppRoutingModule } from './app-routing.module';
import { CacheInterceptor } from './intercepteurs/CacheInterceptor';
import { AppComponent } from './app.component';
import { CoreModule } from './core/core.module';
import { BootstrapModalModule } from 'ng2-bootstrap-modal';
import { RangeSliderModule  } from 'ngx-rangeslider-component';
import { FooterComponent } from './footer/footer.component';

/* Routing Module */

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    CoreModule.forRoot(),
    SharedModule,
    AppRoutingModule,
    BootstrapModalModule,
    RangeSliderModule
  ],
  providers: [
    RequeteCache,
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: CacheInterceptor, multi: true }
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }
