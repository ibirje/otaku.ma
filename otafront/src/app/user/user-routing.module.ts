import { CommandesComponent } from './commandes/commandes.component';
import { ProfileComponent } from './profile/profile.component';
import { AuthGuard } from './../guards/auth.guard';
import { ModifierPasswordComponent } from './modifier-password/modifier-password.component';
import { VerifierEmailComponent } from './verifier-email/verifier-email.component';
import { LoginComponent } from './login/login.component';

import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { SendRecupereLoginComponent } from './send-recupere-login/send-recupere-login.component';
import { PanierComponent } from './panier/panier.component';
import { PasserCommandeComponent } from './passer-commande/passer-commande.component';

const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'login', component: LoginComponent},
  {path: 'inscription', component: RegisterComponent},
  {path: 'recupererLogin', component: SendRecupereLoginComponent},
  {path: 'verifemail/:key', component: VerifierEmailComponent},
  {path: 'modifiermotdepasse/:key', component: ModifierPasswordComponent},
  {path: 'panier', component: PanierComponent, canActivate: [AuthGuard]},
  {path: 'profile', component: ProfileComponent, canActivate: [AuthGuard]},
  {path: 'passercommande', component: PasserCommandeComponent, canActivate: [AuthGuard]},
  {path: 'commandes', redirectTo: 'commandes/', pathMatch: 'full' },
  {path: 'commandes/:option', component: CommandesComponent, canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule {}
