import { Title } from '@angular/platform-browser';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  constructor( private titleService: Title) { 
    this.titleService.setTitle( 'Profile' ); }

  ngOnInit() {
  }

}
