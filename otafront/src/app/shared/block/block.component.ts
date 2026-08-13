import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-block',
  templateUrl: './block.component.html',
  styleUrls: ['./block.component.css']
})
export class BlockComponent implements OnInit {

  private _color: string;
  @Input() index = 0;
  @Input() link: string;
  @Input() cols = 'col-md-3 col-lg-3 col-sm-6 col-xs-6';
  colors = ['blue-block', 'pistache-block', 'orange-block', 'green-block', 'pink-block', 'yellow-block', 'red-block'];

  @Input() get color(): string {
    if (!this._color) {
      this._color =  this.colors[this.index % this.colors.length];
    }
    return this._color;
  }
  set color(c) { this._color = c; }
  constructor() { }

  ngOnInit() {
  }

}
