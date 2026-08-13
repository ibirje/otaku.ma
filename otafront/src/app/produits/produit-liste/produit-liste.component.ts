import { Component, OnInit, Input } from '@angular/core';
import { Produit } from 'src/app/data/Produit';

@Component({
  selector: 'app-produit-liste',
  templateUrl: './produit-liste.component.html',
  styleUrls: ['./produit-liste.component.css'],
})
export class ProduitListeComponent implements OnInit {

  @Input() public produits: Produit[] ;
  @Input() public size: number;
  @Input() public loading = true;
  @Input() listeVideText  = 'Aucun resultat trouvé';

  constructor() {
  }

  ngOnInit() {

  }
}
