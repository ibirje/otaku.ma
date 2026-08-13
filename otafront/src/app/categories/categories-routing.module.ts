import { CategorieListeComponent } from './pages/categorie-liste/categorie-liste.component';
import { CategoriesComponent } from './pages/categories/categories.component';

import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  {path: '', component: CategoriesComponent},
  {path: ':nom', component: CategorieListeComponent} //  canActivate: [AuthGuard],
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CategoriesRoutingModule {}
