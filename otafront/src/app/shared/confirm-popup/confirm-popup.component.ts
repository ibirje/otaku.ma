import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-confirm-popup',
  templateUrl: './confirm-popup.component.html',
  styleUrls: ['./confirm-popup.component.css']
})
export class ConfirmPopupComponent implements OnInit {

  @Input() openPopup: boolean;
  @Output() buttonClickedEvent = new EventEmitter();
  @Output() button2ClickedEvent = new EventEmitter();

  @Input() text: string;
  @Input() buttonText: string;
  @Input() button2Text: string;
  @Input() titre: string;
  constructor() { }
/*
add #id to cmd-item && show function

*/
  ngOnInit() {
  }
  
  closePopup() {
    this.text = null;
    this.openPopup = false;
  }
  handleClick($event) {
    $event.stopPropagation();
  }
  buttonClick() {
    this.buttonClickedEvent.emit();
  }
  button2Click() {
    this.button2ClickedEvent.emit();
  }

}
