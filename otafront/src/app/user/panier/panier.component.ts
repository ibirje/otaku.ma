import { Title } from '@angular/platform-browser';
import { CommandeService } from './../../services/commande.service';
import { UtilisateurService } from './../../services/utilisateur.service';
import { AuthenticationService } from './../../services/authentication.service';
import { FullPanier } from './../../data/restdata/FullPanier';
import { ProduitsService } from './../../services/produits.service';
import { Component, OnInit, ViewChildren, QueryList } from '@angular/core';
import { ItemComponent } from './item/item.component';
import { PanierItem } from 'src/app/data/PanierItem';
import { Router } from '@angular/router';

@Component({
  selector: 'app-panier',
  templateUrl: './panier.component.html',
  styleUrls: ['./panier.component.css']
})
export class PanierComponent implements OnInit {
  
  @ViewChildren(ItemComponent) items: QueryList<ItemComponent>;

  quantiteTotale = 0;
  prixTotal      = 0;
  panierCount    = 0;
  itemlist: FullPanier[];
  loading = true;

  get user() {
    return this.authservice.currentUserValue;
  }

  acheterClick() {

    if (!this.items) { return; }
    const selected: FullPanier[] = [];
    this.items.forEach(item => {
      if ( item.selected) {
        selected.push(item.item);
      }
    });
    if ( selected && selected.length > 0 ) {
      this.passerCommande(selected);
    }
  }

  acheterItemClick($event) {
    const item = $event as FullPanier;
    this.passerCommande([item]);
  }

  passerCommande( list: FullPanier[] ) {
    this.cmdservice.changeCommande(list);
    this.router.navigate(['/user/passercommande']);
  }

  constructor(private service: ProduitsService, private authservice: AuthenticationService,
    private cmdservice: CommandeService, private router: Router, private titleService: Title) { 
      this.titleService.setTitle( 'Panier' );

    service.getPanier().subscribe ((data: {}) => {
      this.itemlist = data as [FullPanier];

      if (this.itemlist && this.itemlist.length > 0) {
        this.user.panierCount = this.itemlist.length;
      } else {
        this.user.panierCount = 0;
      }
      
      this.loading = false;
    });
  }

  ngOnInit() {
  }

  onSelectionChanged() {
    
    this.quantiteTotale = 0;
    this.prixTotal      = 0;

    this.items.forEach(item => {
      if ( item.selected) {
        this.quantiteTotale += item.qte;
        this.prixTotal += item.prix;
      }
    });
  }
  onDelete($event) {
    
    this.loading = true;
    const code = $event;
    this.service.deletePanier({code : code}).subscribe ((data: {}) => {

      this.itemlist = data as [FullPanier];
      this.quantiteTotale = 0;
      this.prixTotal      = 0;
      
      if (this.itemlist && this.itemlist.length > 0) {
        this.user.panierCount = this.itemlist.length;
      } else {
        this.user.panierCount = 0;
      }
      this.loading = false;
    },
    error => { 
      this.loading = false;
    });

    // TODO send delete with code to server
    // TODO if success delete item with code in list
    // ng g c passer commande ( adresses , items , envoi mail, appel) , commandes , details commande
  }
}
