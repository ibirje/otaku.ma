import { AuthenticationService } from './../services/authentication.service';
import { AuthGuard } from './../guards/auth.guard';
import { PseudoPipe } from './../shared/pipes/pseudo.pipe';
import { CategoriesService } from '../services/categories.service';
import { ThemesService } from '../services/themes.service';
import { ProduitsService } from '../services/produits.service';
import { ModuleWithProviders, NgModule, Optional, SkipSelf } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CoreComponent } from './core.component';
import { UtilisateurService } from '../services/utilisateur.service';
import { SeoService } from '../services/seo.service';

@NgModule({
  imports:      [ CommonModule ],
  declarations: [ CoreComponent ],
  exports:      [ CoreComponent ],
  providers:    [ ]
})
export class CoreModule {
  constructor (@Optional() @SkipSelf() parentModule: CoreModule) {
    if (parentModule) {
      throw new Error(
        'CoreModule is already loaded. Import it in the AppModule only');
    }
  }
  
  static forRoot(): ModuleWithProviders {
    return {
      ngModule: CoreModule,
      providers: [
        ProduitsService,
        ThemesService,
        CategoriesService,
        UtilisateurService,
        AuthenticationService,
        SeoService
      ]
    };
  }
}
