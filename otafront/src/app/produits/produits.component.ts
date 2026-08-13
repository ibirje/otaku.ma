import { ProduitsService } from '../services/produits.service';
import { Component, OnInit, Optional } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-produits',
  templateUrl: './produits.component.html'
})
export class ProduitsComponent implements OnInit {

  nom: String;
  constructor(private service: ProduitsService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.nom = this.route.snapshot.paramMap.get('nom');
  }
}
