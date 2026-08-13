import { SharedModule } from './../shared/shared.module';
import { FormsModule } from '@angular/forms';
import { ProduitsRoutingModule } from './produits-routing.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProduitsComponent } from './produits.component';
import { DetailsProduitComponent } from './pages/details-produit/details-produit.component';
import { ProduitSlideshowComponent } from './produit-slideshow/produit-slideshow.component';
import {SlideshowModule} from 'ng-simple-slideshow';
import { ProduitInfosComponent } from './produit-infos/produit-infos.component';
import { VariationsGridComponent } from './variations-grid/variations-grid.component';
import { ProduitSpecificationsComponent } from './produit-specifications/produit-specifications.component';
import { RechercheListeComponent } from './pages/recherche-liste/recherche-liste.component';

@NgModule({
  declarations: 
  [
    ProduitsComponent,
    DetailsProduitComponent,
    ProduitSlideshowComponent,
    ProduitInfosComponent,
    VariationsGridComponent,
    ProduitSpecificationsComponent,
    RechercheListeComponent
  ],
  imports: [
    FormsModule,
    SharedModule,
    SlideshowModule,
    CommonModule,
    ProduitsRoutingModule
  ]
})
export class ProduitsModule { }
