import { ThemeListComponent } from './pages/theme-list/theme-list.component';
import { ThemesComponent } from './pages/themes/themes.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  {path: '', component: ThemesComponent},
  {path: ':nom', component: ThemeListComponent} //  canActivate: [AuthGuard],
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ThemesRoutingModule {}
