import { RegisterComponent } from './register/register.component';
import { UserRoutingModule } from './user-routing.module';
import { LoginComponent } from './login/login.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SendRecupereLoginComponent } from './send-recupere-login/send-recupere-login.component';
import { ModifierPasswordComponent } from './modifier-password/modifier-password.component';
import { VerifierEmailComponent } from './verifier-email/verifier-email.component';
import { PanierComponent } from './panier/panier.component';
import { ItemComponent } from './panier/item/item.component';
import { SharedModule } from '../shared/shared.module';
import { PasserCommandeComponent } from './passer-commande/passer-commande.component';
import { SelectAdresseComponent } from './passer-commande/select-adresse/select-adresse.component';
import { ProfileComponent } from './profile/profile.component';
import { AdresseComponent } from './profile/adresse/adresse.component';
import { ProfileInfoBoxComponent } from './profile/profile-info-box/profile-info-box.component';
import { InfoblocksComponent } from './profile/infoblocks/infoblocks.component';
import { CommandesComponent } from './commandes/commandes.component';
import { CommandesListeItemComponent } from './commandes/commandes-liste-item/commandes-liste-item.component';
import { DetailsCommandeComponent } from './commandes/details-commande/details-commande.component';


@NgModule({
  imports: [
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    UserRoutingModule,
    CommonModule,
    SharedModule
  ],
  declarations: [
      LoginComponent,
      RegisterComponent,
      SendRecupereLoginComponent,
      ModifierPasswordComponent,
      VerifierEmailComponent,
      PanierComponent,
      ItemComponent,
      PasserCommandeComponent,
      SelectAdresseComponent,
      ProfileComponent,
      AdresseComponent,
      ProfileInfoBoxComponent,
      InfoblocksComponent,
      CommandesComponent,
      CommandesListeItemComponent,
      DetailsCommandeComponent
  ]
})

export class UserModule { }
