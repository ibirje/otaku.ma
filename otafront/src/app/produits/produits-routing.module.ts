import { AuthGuard } from './../guards/auth.guard';
import { DetailsProduitComponent } from './pages/details-produit/details-produit.component';
import { ProduitsComponent } from './produits.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RechercheListeComponent } from './pages/recherche-liste/recherche-liste.component';

const routes: Routes = [
  {path: '', component: ProduitsComponent},
  {path: 'recherche/:titre', component: RechercheListeComponent} ,
  {path: ':nom', component: DetailsProduitComponent} //  canActivate: [AuthGuard],
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProduitsRoutingModule {}
