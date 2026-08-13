import { CommandeItem } from './../../../data/CommandeItem';
import { Commande } from './../../../data/Commande';
import { FullCommande } from './../../../data/restdata/FullCommande';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-details-commande',
  templateUrl: './details-commande.component.html',
  styleUrls: ['./details-commande.component.css']
})
export class DetailsCommandeComponent implements OnInit {


  @Input() openPopup: boolean;
  @Input() commande: Commande;
  @Input() items: CommandeItem[];

  constructor() { }

  ngOnInit() { }
  
  closePopup() {
    
    this.commande = null;
    this.openPopup = false;
  }

  handleClick($event) {
    $event.stopPropagation();
  }
}
