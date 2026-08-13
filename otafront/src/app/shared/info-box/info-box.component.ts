import { AuthenticationService } from './../../services/authentication.service';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { UtilisateurService } from 'src/app/services/utilisateur.service';

@Component({
  selector: 'app-info-box',
  templateUrl: './info-box.component.html',
  styleUrls: ['./info-box.component.css']
})
export class InfoBoxComponent implements OnInit {

  @Input() titre;
  @Input() selectable = true;
  @Input() public selected = false;
  @Output() selectEvent = new EventEmitter();
  @Output() expandEvent = new EventEmitter();
  
  
  constructor() { 

  }

  ngOnInit() {

  }
  expand() {
    this.expandEvent.emit();
  }
  select() {
    if ( ! this.selected ) {
      this.selected = true;
      this.selectEvent.emit();
    }
  }
}
