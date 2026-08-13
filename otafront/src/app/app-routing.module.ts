import { HomeComponent } from './home/home.component';


import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

export const routes: Routes = [
  { path: '',  component: HomeComponent },
  { path: 'user', children: [ { path : '', loadChildren : './user/user.module#UserModule' }] },
  { path: 'produits', loadChildren: './produits/produits.module#ProduitsModule' },
  { path: 'categories', loadChildren: './categories/categories.module#CategoriesModule' },
  { path: 'themes', loadChildren: './themes/themes.module#ThemesModule' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
