import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-type-list',
  templateUrl: './type-list.component.html',
  styleUrls: ['./type-list.component.css']
})
export class TypeListComponent implements OnInit {

  @Input() types;
  
  constructor() { }

  ngOnInit() {
  }

}
