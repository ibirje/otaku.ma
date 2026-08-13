import { Categorie } from './../../data/Categorie';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-subtype-dropdown',
  templateUrl: './subtype-dropdown.component.html',
  styleUrls: ['./subtype-dropdown.component.css']
})
export class SubtypeDropdownComponent implements OnInit {

  @Input() item: {parent: Categorie, childs: Categorie[]};

  constructor() { }

  ngOnInit() {
  }

}
