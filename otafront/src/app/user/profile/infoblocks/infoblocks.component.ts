import { ActivatedRoute } from '@angular/router';
import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-infoblocks',
  templateUrl: './infoblocks.component.html',
  styleUrls: ['./infoblocks.component.css']
})
export class InfoblocksComponent implements OnInit {

  url: string;
  @Output() clicked = new EventEmitter();
  constructor(private route: ActivatedRoute) { 
    const url = this.route.snapshot.url.toString();
  }

  ngOnInit() {
  }

  urlChanged() {
    
    const cururl = this.route.snapshot.url.toString();
    if ( !cururl.startsWith('commandes') || this.url === cururl) { return; }
    this.url = cururl;
    const option = cururl.substring('commandes/'.length).replace(/-/g, ' ');
    this.clicked.emit(option);
  }
}
