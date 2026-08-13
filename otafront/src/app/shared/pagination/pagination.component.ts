import { Const } from './../const-common';
import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent implements OnInit {

  private _size;
  private taillePage = Const.LIST_COUNT;
  @Output() public changedEvent = new EventEmitter();

  _minpage = 1;
  _maxpage = 1;
  rangemin = 1;
  rangemax;
  _page = 1;

  get page() {
    return this._page;
  }
  
  @Input() set page(pg) {

    if ( pg == null ) {
      pg = 1;
    }
    this._page = pg;
  }

  public get size() {
    return this._size;
  }
  
  @Input() public set size(sz: number) {
    this._size = sz;
    this._maxpage = sz <= this.taillePage ? 1 : Math.floor(sz / this.taillePage);

    if (this.taillePage * this._maxpage < sz ) {
      this._maxpage++;
    }
    this.setuppage(this.page);
  }

  gopage(nb) {
    event.stopPropagation();
    this.page = nb;
    this.setuppage(this.page);
    this.changedEvent.emit(this.page);
    
  }
  gopagenombre(nb) {
    event.stopPropagation();
    this.gopage(nb + this.rangemin);
  }


  setuppage(pg) {

    const range = 5; // 1 3 5 7 9 ...
    
    if (pg >= this._maxpage) {
      pg = this._maxpage;
    }
    if (pg <= this._minpage) {
      pg = this._minpage;
    }
    this._page = pg;

    this.rangemax = this._page === this._minpage ? this._page + (range - 1 ) : this._page + (range - 1 ) / 2;
    this.rangemin = this._page === this._maxpage ? this._page - (range - 1 ) :  this._page - (range - 1 ) / 2;
    
    this.rangemax = this.rangemax >= this._maxpage ? this._maxpage : this.rangemax;
    this.rangemin = this.rangemin >= this._minpage ? this.rangemin : this._minpage;
    /*
    console.log('page = ' + this.page + ', range = (' + this.rangemin + ', ' + this.rangemax + 
    ') bounds = (' + this._minpage + ', ' + this._maxpage + ')');
    */
  }

  constructor() { }

  ngOnInit() {
  }

  counter(i: number) {
    return new Array(i);
}
}
