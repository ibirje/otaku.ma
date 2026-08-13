import { AdressePipe } from './../../../shared/pipes/adresse.pipe';
import { PhonePipe } from './../../../shared/pipes/phone.pipe';
import { NomPipe } from './../../../shared/pipes/nom.pipe';
import { Adresse } from './../../../data/adresse';
import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { UtilisateurService } from 'src/app/services/utilisateur.service';

@Component({
  selector: 'app-adresse',
  templateUrl: './adresse.component.html',
  styleUrls: ['./adresse.component.css']
})
export class AdresseComponent implements OnInit {

  expanded = false;
  newaddr;
  _adresse: any;
  @Input() public titre;
  @Output() public selectionEvent = new EventEmitter();
  public selected = false;
  
  private _nom;
  private _prenom;
  private _adresse1;
  private _adresse2;
  private _ville;
  private _telephone;
  private _codePostal;

  err_nom        = false;
  err_prenom     = false;
  err_adresse1   = false;
  err_adresse2   = false;
  err_ville      = false;
  err_telephone  = false;
  err_codePostal = false;
  err_serveur;
  success;
  
  set nom(n: string) { this._nom = n; this.nomChanged(); }
  get nom() { return this._nom; }

  set prenom(n: string) { this._prenom = n; this.prenomChanged(); }
  get prenom() { return this._prenom; }

  set telephone(n: string) { this._telephone = n; this.phoneChanged(); }
  get telephone() { return this._telephone; }
  
  set adresse1(n: string) { this._adresse1 = n; this.adresse1Changed(); }
  get adresse1() { return this._adresse1; }
  
  set adresse2(n: string) { this._adresse2 = n; } // this.adresse2Changed(); verif adresse 2 ???
  get adresse2() { return this._adresse2; }

  set ville(n: string) { this._ville = n; this.villeChanged(); }
  get ville() { return this._ville; }

  set codePostal(n: string) { this._codePostal = n; this.codePostalChanged(); }
  get codePostal() { return this._codePostal; }
  
  nomChanged() { this.err_nom = this.adresse.nom !== '' && this._nom === '' ? true : 
    this._nom !== new NomPipe().transform(this._nom); this.success = null;
  }
  prenomChanged() { this.err_prenom = this.adresse.prenom !== '' && this._prenom === '' ? true : 
    this._prenom !== new NomPipe().transform(this._prenom); this.success = null; 
  }
  phoneChanged() { this.err_telephone = this.adresse.telephone1 !== '' && this._telephone === '' ? 
    true : this._telephone !== new PhonePipe().transform(this._telephone); this.success = null;
  }
  adresse1Changed() { this.err_adresse1 = this.adresse.adresse1 !== '' && this._adresse1 === '' ? 
    true : this._adresse1 !== new AdressePipe().transform(this._adresse1); this.success = null;
  }
  villeChanged() { this.err_ville = this.adresse.ville !== '' && this._ville === '' ? 
    true : this._ville !== new NomPipe().transform(this._ville); this.success = null;
  }
  codePostalChanged() { this.err_codePostal =  this._codePostal === '' ? true : false; this.success = null;
  }
  
  get hasError() {
    return this.err_adresse1 || this.err_adresse2 || this.err_nom || 
    this.err_prenom || this.err_telephone || this.err_ville || this.err_serveur || this.err_codePostal;
  }
  

  constructor( private userservice: UtilisateurService ) {
    this.newaddr = new Adresse();
    this.adresse = this.newaddr;
   }

  ngOnInit() { 
  }
  
  @Input() set adresse(addr) {
    this._adresse = addr;
    if (this._adresse) {
      this.nom = this._adresse.nom =  this._adresse.nom ? this._adresse.nom  : '';
      this.prenom = this._adresse.prenom =  this._adresse.prenom ? this._adresse.prenom  : '';
      this.adresse1 = this._adresse.adresse1 =  this._adresse.adresse1 ? this._adresse.adresse1  : '';
      this.adresse2 = this._adresse.adresse2 =  this._adresse.adresse2 ? this._adresse.adresse2  : '';
      this.ville = this._adresse.ville =  this._adresse.ville ? this._adresse.ville  : '';
      this.telephone = this._adresse.telephone1 =  this._adresse.telephone1 ? this._adresse.telephone1  : '';
      this.codePostal = this._adresse.codePostal =  this._adresse.codePostal ? this._adresse.codePostal  : '';
    }
  }
  get adresse() {
    return this._adresse;
  }


  get saveEnabled() {
    return !this.err_nom && !this.err_prenom  && !this.err_adresse1 && !this.err_adresse2 && 
    !this.err_ville && !this.err_telephone && !this.err_codePostal &&
    (!this.adresse || (this.nom !== this.adresse.nom || this.prenom !== this.adresse.prenom ||
      this.adresse1 !== this.adresse.adresse1 || this.ville !== this.adresse.ville || 
      this.telephone !== this.adresse.telephone1 || this.codePostal !== this.adresse.codePostal));
  }
  enregistrer() {
    this.success = this.err_serveur = null;
    if ( !this.saveEnabled ) { return; }

    const newaddr = { nom : this.nom, prenom : this.prenom, adresse1 : this.adresse1, 
      adresse2 : this.adresse2, ville : this.ville, telephone1: this.telephone, codePostal: this.codePostal };
    if (this.adresse && this.adresse !== this.newaddr) {
      this.userservice.updateAdresse([ this.adresse, newaddr]).subscribe ((data: {}) => {
        this.success = 'Adresse modifiée.';
    
        this.adresse.nom = this.nom;
        this.adresse.prenom = this.prenom;
        this.adresse.adresse1 = this.adresse1;
        this.adresse.adresse2 = this.adresse2;
        this.adresse.ville = this.ville;
        this.adresse.telephone1 = this.telephone;
        this.adresse.codePostal = this.codePostal;
      }, error => {
        /* TODO switch erreurs serveur */
        // console.log(error);
        this.err_serveur = 'Echec de modification de l\'adresse'; 
      });

    } else {

      this.userservice.saveAdresse(newaddr).subscribe ((data: {}) => {
        this.success = 'Adresse modifiée.';
        this.adresse = newaddr;
        
        }, error => {
          /* TODO switch erreurs serveur */
          this.err_serveur = 'Echec de modification de l\'adresse'; 
        });
    }
  }

  selectionChanged() { 
    this.selected = true; this.selectionEvent.emit(this.titre);
    if (this.adresse) {
      (this.adresse as Adresse).etat = 'SELECTED';
      this.userservice.selectAdresse(this.adresse).subscribe ((data: {}) => {
        
      }, error => {
        this.err_serveur = 'Echec de selection'; 
      });
    }
    // SEND REQUETE UPDATE ADRESSE SELECTION = 1 oTHERS = 0 //DEFAULT
  }
  
  expandedClick() { this.expanded = ! this.expanded; }
}
