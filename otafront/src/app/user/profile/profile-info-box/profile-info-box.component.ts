import { PhonePipe } from './../../../shared/pipes/phone.pipe';
import { NomPipe } from './../../../shared/pipes/nom.pipe';
import { PseudoPipe } from './../../../shared/pipes/pseudo.pipe';
import { UtilisateurService } from 'src/app/services/utilisateur.service';
import { AuthenticationService } from './../../../services/authentication.service';
import { Component, OnInit, Input } from '@angular/core';


@Component({
  selector: 'app-profile-info-box',
  templateUrl: './profile-info-box.component.html',
  styleUrls: ['./profile-info-box.component.css']
})
export class ProfileInfoBoxComponent implements OnInit {

  expanded = false;

  @Input() public titre = 'Informations';
  private _nom: string; 
  private _prenom: string;
  private _telephone: string; 
  private _pseudo: string;

  private _old_mdp: string;
  private _new_mdp: string;
  private _new_mdp_confirm: string;
  
  err_nom       = false;
  err_prenom    = false;
  err_telephone = false;
  err_pseudo    = false;
  err_serveur;
  success;
  
  err_pwd; 
  err_pwd_srv;
  pwd_success;
  
  set nom(n: string) { this._nom = n; this.nomChanged(); }
  get nom() { return this._nom; }

  set prenom(n: string) { this._prenom = n; this.prenomChanged(); }
  get prenom() { return this._prenom; }

  set telephone(n: string) { this._telephone = n; this.phoneChanged(); }
  get telephone() { return this._telephone; }
  
  set pseudo(n: string) { this._pseudo = n; this.pseudoChanged(); }
  get pseudo() { return this._pseudo; }

  pseudoChanged() { this.err_pseudo = this.pseudo !== new PseudoPipe().transform(this.pseudo); this.success = null; }
  
  nomChanged() { this.err_nom = this.user.nom !== '' && this.nom === '' ? true : 
    this.nom !== new NomPipe().transform(this.nom); this.success = null;
  }
  prenomChanged() { this.err_prenom = this.user.prenom !== '' && this.prenom === '' ? true : 
    this.prenom !== new NomPipe().transform(this.prenom); this.success = null;
  }
  phoneChanged() { this.err_telephone = this.user.telephone1 !== '' && this.telephone === '' ? 
      true : this.telephone !== new PhonePipe().transform(this.telephone); this.success = null;
  }
  
  set old_mdp(str: string) { this._old_mdp = str; this.oldMdpChanged(); }
  get old_mdp(): string { return this._old_mdp; }
  
  set new_mdp(str: string) { this._new_mdp = str; this.newMdpChanged(); }
  get new_mdp(): string { return this._new_mdp; }

  set new_mdp_confirm(str: string) { this._new_mdp_confirm = str; this.newMdpConfirmChanged(); }
  get new_mdp_confirm(): string { return this._new_mdp_confirm; }
  
  oldMdpChanged() { this.verifMdps(); }
  newMdpChanged() { this.verifMdps(); }
  newMdpConfirmChanged() { this.verifMdps(); }

  verifMdps() {
    this.pwd_success = this.err_pwd = this.err_pwd_srv = null;
    if (this._new_mdp && this._new_mdp !== '' && this._new_mdp_confirm && this._new_mdp_confirm !== '') {
      if (this._new_mdp.length < 6 ) { 
        this.err_pwd = 'Le nouveau mot de passe doit contenir aumoins 6 caractères.'; 
      } else { 
        if (this._new_mdp !== this.new_mdp_confirm) { 
          this.err_pwd = 'La confirmation du nouveau mot de passe est différente du nouveau mot de passe.'; 
        } else if ( !this._old_mdp || this._old_mdp === '') { this.err_pwd = 'Mot de passe courant vide.'; 
        } else if (this._old_mdp.length < 6) { this.err_pwd = 'Le mot de passe courant doit contenir aumoins 6 caractères.'; }
      }
      // TODO validation regex mdp
    } 
  }


  get saveEnabled() {
    return !this.err_nom && !this.err_prenom && !this.err_telephone && !this.err_pseudo &&
      (this.nom       !== this.user.nom || 
      this.prenom    !== this.user.prenom || 
      this.telephone !== this.user.telephone1 || 
      this.pseudo    !== this.user.pseudo);
  }
  get hasError() {
    return this.err_pseudo || this.err_nom || this.err_prenom || this.err_telephone;
  }

  get pwd_saveEnabled() {
    return !this.err_pwd && !this.err_pwd_srv && !this.pwd_success &&
    this._new_mdp && this._new_mdp !== '' && this._new_mdp_confirm && this._new_mdp_confirm !== '';
  }

  get user() {
    return this.authservice.currentUserValue;
  }

  constructor(private authservice: AuthenticationService, private userservice: UtilisateurService ) { 

  }

  ngOnInit() {
    if (this.user) {
      this.user.nom        = !this.user.nom        || this.user.nom        === '' ? '' : this.user.nom;
      this.user.prenom     = !this.user.prenom     || this.user.prenom     === '' ? '' : this.user.prenom;
      this.user.telephone1 = !this.user.telephone1 || this.user.telephone1 === '' ? '' : this.user.telephone1;
      this.user.pseudo     = !this.user.pseudo     || this.user.pseudo     === '' ? '' : this.user.pseudo;
      
      this._nom       = this.user.nom;
      this._prenom    = this.user.prenom;
      this._telephone = this.user.telephone1;
      this._pseudo    = this.user.pseudo;
    }
  }

  expandedClick() {
    this.expanded = ! this.expanded;
  }
  enregistrer() {
    this.success = null;
    if ( !this.saveEnabled ) { return; }
    const psd = this.user.pseudo || this.user.pseudo === '' ? this.pseudo : this.user.pseudo;
    
    const user = { nom : this.nom, prenom : this.prenom , telephone1 : this.telephone, email : this.user.email, pseudo : psd};
    this.userservice.update(user).subscribe ((data: {}) => {
      this.success = 'Profile modifié.';

      this.user.pseudo = user.pseudo;
      this.user.nom = user.nom;
      this.user.prenom = user.prenom;
      this.user.telephone1 = user.telephone1;
      
    }, error => {
      /* TODO switch erreurs serveur */
      this.err_serveur = 'Echec de modification de profile'; 
    });
  }
  enregistrerMdp() {
    this.pwd_success = null;
    if ( !this.pwd_saveEnabled ) { return; }

    const changemdp = { email : this.user.email, motdepasse : this._old_mdp, nouveau: this._new_mdp };

    this.userservice.updateMotDePasse(changemdp).subscribe ((data: {}) => {
      this.pwd_success = 'Mot de passe modifié.';
    }, error => {
      /* TODO switch erreurs serveur */
      this.err_pwd_srv = 'Echec de modification du mot de passe.'; 
    });
  }
}
