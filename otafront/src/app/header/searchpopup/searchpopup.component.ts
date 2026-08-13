import { Const } from './../../shared/const-common';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { BehaviorSubject, Subscription } from 'rxjs';
import { ProduitsService } from './../../services/produits.service';
import { Component, OnInit, Output, Input } from '@angular/core';
import { _MatSlideToggleMixinBase } from '@angular/material';

@Component({
  selector: 'app-searchpopup',
  templateUrl: './searchpopup.component.html',
  styleUrls: ['./searchpopup.component.css'],
})
export class SearchpopupComponent implements OnInit {

  @Input() visible = false;
  _text: string;
  
  filtres: BehaviorSubject<string>;
  filtrechanges: Subscription;

  items: { nom: string , code: string , link: string , count: number  }[] = [];
  

  @Input() get text(): string  { return this._text; }
  set text(str: string)  { 
    this._text = str; 
    this.visible = (this._text && this._text.trim().length >= Const.MIN_LENGTH_RECHERCHE ); 
    if ( this.visible) { this.textChanged(); }
  }

  constructor(private service: ProduitsService) {

    this.filtres = new BehaviorSubject<string>('');

    this.filtrechanges = this.filtres.pipe(debounceTime(1000), distinctUntilChanged()).subscribe(c => {

      if (this.text && this.text.length >= Const.MIN_LENGTH_RECHERCHE ) {
        this.service.getSuggestions(this.text).subscribe(data => {
          this.items = data as { nom: string , code: string , link: string , count: number  }[];
        });
      }
    });
  }
  
  ngOnInit() { }
  
  textChanged() { this.filtres.next(this.text); }
  clickedOutside() { this.visible = false; }

}
