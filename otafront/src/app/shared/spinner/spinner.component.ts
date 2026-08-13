import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.css']
})
export class SpinnerComponent implements OnInit {

  _valeur = 1;
  @Input() minimum = 1;
  @Input() maximum = 100;
  @Output() quantiteChanged = new EventEmitter();

  constructor() { 

  }

  ngOnInit() {
  
  }

  @Input() set valeur(v) {
    this._valeur = v;
    if (this.valeur > this.maximum) {
      this.valeur = this.maximum;
    }
    if (this.valeur < this.minimum) {
      this.valeur = this.minimum;
    }
    this.quantiteChanged.emit(this.valeur);
  }
  get valeur() {
    return this._valeur;
  }
  plus() {
    this.valeur ++;
  }
  moins() {
    this.valeur --;
  }
}
