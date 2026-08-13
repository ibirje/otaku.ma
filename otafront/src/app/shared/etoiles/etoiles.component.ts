import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-etoiles',
  templateUrl: './etoiles.component.html',
  styleUrls: ['./etoiles.component.css']
})
export class EtoilesComponent implements OnInit {

  @Input() public avis;
  @Input() public size = 'size-sous-titre'; 
  nombreetoiles;
  nombreetoilesvides;


  @Input() public set nombre(nbr: any) {
    this.nombreetoiles      = Array(nbr).fill(1);
    if ( 5 - nbr > 0 ) {
      this.nombreetoilesvides = Array(5 - nbr).fill(1);
    }
  }
  public get nombre() {
    if (this.nombreetoiles ) {
      return this.nombreetoiles.length;
    }
    return 0;
  }

  constructor() { }

  ngOnInit() {
  }

}
