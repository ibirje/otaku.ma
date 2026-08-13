import { Component, OnInit, EventEmitter, Output } from '@angular/core';
@Component({
  selector: 'app-list-filtres',
  templateUrl: './list-filtres.component.html',
  styleUrls: ['./list-filtres.component.css']
})
export class ListFiltresComponent implements OnInit {

  @Output() triChanged = new EventEmitter();
  @Output() prixChanged = new EventEmitter();
  tri = 'pertinence';

  constructor() { }

  ngOnInit() {
  }
  triClick(tri: string) {
    this.tri = tri;
    this.triChanged.emit(tri);
  }
  prixRangeEvent($event) {
    this.prixChanged.emit($event);
  }
}
