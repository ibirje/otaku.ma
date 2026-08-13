import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-popup',
  templateUrl: './popup.component.html',
  styleUrls: ['./popup.component.css']
})
export class PopupComponent implements OnInit {

  @Input() openPopup: boolean;
  @Output() buttonClickedEvent = new EventEmitter();

  @Input() buttonText: string;
  @Input() titre: string;

  @Input() text1: string;
  @Input() text2: string;
  @Input() image: string;

  
  constructor() { }

  ngOnInit() {
  }
  closePopup() {
    this.text1 = this.text2 = this.image =  null;
    this.openPopup = false;
  }
  handleClick($event) {
    $event.stopPropagation();
  }
  buttonClick() {
    this.buttonClickedEvent.emit();
  }
}
