import { Title } from '@angular/platform-browser';

import { FullCommande } from './../../data/restdata/FullCommande';
import { Commande } from './../../data/Commande';
import { ActivatedRoute } from '@angular/router';
import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { CommandeService } from 'src/app/services/commande.service';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { BehaviorSubject, Subscription } from 'rxjs';
import { DetailsCommandeComponent } from './details-commande/details-commande.component';

@Component({
  selector: 'app-commandes',
  templateUrl: './commandes.component.html',
  styleUrls: ['./commandes.component.css']
})
export class CommandesComponent implements OnInit, OnDestroy {
  

  @ViewChild('detailsCommande') details: DetailsCommandeComponent;
  _code: string;
  commandes: FullCommande[];
  cmd: Commande;
  option: string;
  page = 1;

  filtres: BehaviorSubject<string>;
  filtrechanges: Subscription;



  get code(): string { return this._code; }
  set code(c: string) { this._code = c; this.filtreChanged(); }

  constructor(private service: CommandeService, private route: ActivatedRoute, private titleService: Title) { 
    this.titleService.setTitle( 'Commandes' ); 

    this.option = this.route.snapshot.url.toString().substring('commandes/'.length).replace(/-/g, ' ');
    
    this.filtres = new BehaviorSubject<string>('');

    this.filtrechanges = this.filtres.pipe(debounceTime(1000), distinctUntilChanged()).subscribe(c => {
      this.service.getCommandes(this.option, this.code, this.page).subscribe(data => {this.commandes = data as FullCommande[]; });
    });

    // TODO onclick or detect change url refresh getcommandes

    // add function to server , rince & flush
    // fix commande liste item, fix buttons, fix details commande
    // 
  }


  urlchanged($event) {
    this.option = $event;
    this.service.getCommandes(this.option, this.code, this.page)
    .subscribe(data => {this.commandes = data as FullCommande[]; });
  }
  showDetails($event) {
    
    const cmd: FullCommande = $event;
    if (cmd) {
      console.log(cmd.commande);  
      this.details.commande = cmd.commande;
      this.details.items = cmd.items;
      this.details.openPopup = true;
    }
  }
  filtreChanged() { this.filtres.next(this.code); }

  ngOnInit() {  }
  ngOnDestroy() { this.filtres.unsubscribe(); }
}
