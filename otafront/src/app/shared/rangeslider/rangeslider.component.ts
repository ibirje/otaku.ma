import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Const } from '../const-common';

@Component({
  selector: 'app-rangeslider',
  templateUrl: './rangeslider.component.html',
  styleUrls: ['./rangeslider.component.css']
})
export class RangesliderComponent implements OnInit {



  min = 0;
  max = 100;
  twoWayRange = [0, 100];
  dragged: boolean;
  lowerlimit = Const.FILTRE_PRIX_MIN;
  upperlimit = Const.FILTRE_PRIX_MAX;
  @Output() public changedEvent = new EventEmitter();

  changed() {
    this.twoWayRange = [...this.twoWayRange];
  }

  constructor() { }

  ngOnInit() {

  }
  
  get minprix() {
    const prix = (1 / 200) * Math.pow (this.twoWayRange[0], 3);
    return Math.floor(prix) + 0.5 < prix ? Math.floor(prix) + 1 : Math.floor(prix);
  }
  set minprix(pr) {

  }
  get maxprix() {
    const prix = (1 / 200) * Math.pow (this.twoWayRange[1], 3);
    return Math.floor(prix) + 0.5 < prix ? Math.floor(prix) + 1 : Math.floor(prix);
  }
  set maxprix(pr) {
    
  }

  onMinKeyup(event) {
    if (event.key === 'Enter') {
      let prix = parseFloat(event.target.value);
      if ( prix <= this.maxprix ) {
        prix = prix >= this.lowerlimit ? prix : this.lowerlimit;
        this.twoWayRange = [ Math.pow ((prix) * 200, 1 / 3), this.twoWayRange[1]];
        this.onPrixChanged();
      }
    }
  }
  
  onMaxKeyup(event) {
    if (event.key === 'Enter') {
      let prix = parseFloat(event.target.value);
      if ( prix >= this.minprix ) {
        prix = prix <= this.upperlimit ? prix : this.upperlimit;
        this.twoWayRange = [this.twoWayRange[0], Math.pow ((prix ) * 200, 1 / 3)];
        this.onPrixChanged();
      }
    }
  }

  onPrixChanged() {
    this.changedEvent.emit( [this.minprix, this.maxprix] );
    // console.log('final range : ' + this.minprix + ' ~ ' + this.maxprix);
  }
  sliderDragged() {
    this.dragged = true;
  }
  sliderMouseUp() {
    if ( this.dragged ) {
      this.onPrixChanged();
    }
    this.dragged = false;
  }









/*

  _min = 0;
  _max = 100;
  _minprix;
  _maxprix;

  
  set min(mi) {
    this._min = mi; 
    if (mi >= this._max) {
      this.minReset();
    } 
  }
  get min() {
    return this._min;
  }
  set max(mx) {
    this._max = mx; 
    if (mx <= this._min) {
      this.maxReset();
    } 
  }
  get max() {
    return this._max;
  }

  
  minReset() {
    if (this.min >= this.max) {
      event.preventDefault();
      event.stopPropagation();
    }
  }
  maxReset() {
    if (this.max <= this.min) {
      event.preventDefault();
      event.stopPropagation();
    }
  }
  minKeyup($event) {
    if (this.min >= this.max) {
      console.log($event);
      if (this.min > this.max) {
        this.min = this.max - 0.1; 
      }
      event.preventDefault();
      event.stopPropagation();
    }
  }
  
  maxKeyup($event) {
    if (this.min >= this.max) {
      console.log($event);
      if (this.min > this.max) {
        this.max = this.min + 0.1; 
      }
      event.preventDefault();
      event.stopPropagation();
    }
  }
  */
}
