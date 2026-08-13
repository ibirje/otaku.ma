import { Component, OnInit, Input, Output } from '@angular/core';
import { timer } from 'rxjs';
import { EventEmitter } from '@angular/core';

@Component({
  selector: 'app-countdown',
  templateUrl: './countdown.component.html',
  styleUrls: ['./countdown.component.css']
})
export class CountdownComponent implements OnInit {

  @Input() datedebut: Date;
  @Input() datefin: Date;
  private countdown = 0;
  private cdtimer;

  jours    = 0;
  minutes  = 0;
  heures   = 0;
  secondes = 0;

  MINUTE_TO_SECONDE = 60;
  HEURE_TO_SECONDE  = 60 * 60;
  JOUR_TO_SECONDE   = 24 * 60 * 60;

  @Output() public promoEndEvent = new EventEmitter();
  @Input() set isPromo(ispromo) {
    
    if (ispromo) {
      this.start();
    } else {
      this.promoEndEvent.emit('promofini');
    }
  }


  constructor() { }

  ngOnInit() {
  }
  start() {

      this.countdown = (this.datefin.getTime() - new Date().getTime()) / 1000;

      this.jours   = this.countdown / this.JOUR_TO_SECONDE;
      let reste = this.countdown % this.JOUR_TO_SECONDE;

      this.heures = reste / this.HEURE_TO_SECONDE;
      reste = reste % this.HEURE_TO_SECONDE;

      this.minutes = reste / this.MINUTE_TO_SECONDE;
      this.secondes = reste % this.MINUTE_TO_SECONDE;


      this.cdtimer = timer(500, 1000);

      this.cdtimer.subscribe(tick => {

        this.secondes--;

        if ( this.secondes < 0 ) {
          this.minutes--;
          this.secondes = 59;
          }
        if ( this.minutes < 0 ) {
          this.heures--;
          this.minutes  = 59 ;
        }
        if ( this.heures < 0 ) {
          this.jours --;
          this.heures = 23;
        }
        if (this.jours < 0) {
          this.isPromo = false;
          this.jours = this.heures = this.minutes = this.secondes = 0;
        }

      });
  }
  
}
