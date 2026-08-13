import { SlideshowModule } from 'ng-simple-slideshow';
import { SharedModule } from './../shared/shared.module';
import { CategoriesRoutingModule } from './categories-routing.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CategoriesComponent } from './pages/categories/categories.component';
import { CategorieListeComponent } from './pages/categorie-liste/categorie-liste.component';

@NgModule({
  declarations: [
    CategoriesComponent, 
    CategorieListeComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    SlideshowModule,
    CategoriesRoutingModule
  ]
})
export class CategoriesModule { }
