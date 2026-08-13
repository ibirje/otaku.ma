import { Component, OnInit } from '@angular/core';
import { Meta } from '@angular/platform-browser';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  backgroundImg = 'https://images2.imgbox.com/ec/d7/0mXxDskR_o.jpg';
  defaultBgImg  = 'https://images2.imgbox.com/ec/d7/0mXxDskR_o.jpg';

  constructor() {
    // Meta
   }

  ngOnInit() {
    
  }

}
