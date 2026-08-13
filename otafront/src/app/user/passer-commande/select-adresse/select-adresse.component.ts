import { Adresse } from './../../../data/adresse';
import { UtilisateurService } from './../../../services/utilisateur.service';
import { AdresseComponent } from './../../profile/adresse/adresse.component';
import { Component, OnInit, ViewChildren, QueryList } from '@angular/core';

@Component({
  selector: 'app-select-adresse',
  templateUrl: './select-adresse.component.html',
  styleUrls: ['./select-adresse.component.css']
})
export class SelectAdresseComponent implements OnInit {

  adresses: Adresse[];
  @ViewChildren(AdresseComponent) items: QueryList<AdresseComponent>;

  constructor(private service: UtilisateurService) { 
    service.getAdresses().subscribe(data => {
      this.adresses = data as Adresse[];
      
      if (this.adresses && this.items) {
        let id = 0;
        this.items.forEach(item => {
          if (this.adresses.length > id) {
            item.selected = this.adresses[id].etat && this.adresses[id].etat  === 'SELECTED';
            item.adresse  = this.adresses[id];
            id++;
          } else { return; }
        });
      }
    },
    error => {

    });
  }

  ngOnInit() {
  }
  selectionChanged($event) {
    const titre = $event;
    this.items.forEach(item => {
      if ( item.selected && item.titre !== titre) { item.selected = false; }
    });
  }
  public get selectedAdresse() {
    let addr: Adresse = null;
    if (this.items && this.items.length > 0) {
      
      this.items.forEach(item => {
        if ( item.selected) { addr = item.adresse; }
      });
    }
    return addr;
  }
}
