import { IImage } from 'ng-simple-slideshow';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ServerSettings } from 'src/app/services/const-server';

@Component({
  selector: 'app-carousel',
  templateUrl: './carousel.component.html',
  styleUrls: ['./carousel.component.css']
})
export class CarouselComponent implements OnInit {


  @ViewChild('slideshow') slideshow: any;
  @ViewChild('block') block: any;
  imageUrls: (string | IImage)[] = [ 
    { url: 'https://images2.imgbox.com/b2/bc/ZaXddwsU_o.png', href : '/themes/Mangas' , backgroundPosition: 'center' },
    { url: 'https://images2.imgbox.com/24/54/P1VMy2hn_o.png', href : '/themes/Jeux-Vidéo', backgroundPosition: 'center' },
    { url: 'https://images2.imgbox.com/9a/95/Hb9yxlGQ_o.png', href : '/categories/Divertissement', backgroundPosition: 'center' }
  ];

  height: String;
  minHeight: String;
  arrowSize: String = '20px';
  showArrows: Boolean = true;
  disableSwiping: Boolean = false;
  autoPlay: Boolean = true;
  autoPlayInterval: Number = 9999;
  stopAutoPlayOnSlide: Boolean = true;
  debug: Boolean = false;
  backgroundSize: String = 'cover';
  backgroundPosition: String = 'center center';
  backgroundRepeat: String = 'no-repeat';
  showDots: Boolean = true;
  dotColor: String = '#FFF';
  showCaptions: Boolean = true;
  captionColor: String = '#FFF';
  captionBackground: String = 'rgba(0, 0, 0, .35)';
  lazyLoad: Boolean = false;
  hideOnNoSlides: Boolean = false;

  ngOnInit() {
    this.height = '' + (this.block.nativeElement.offsetWidth / 4 ) ;
  }
  onResize(event) {

    this.height = '' + (this.block.nativeElement.offsetWidth / 4 );
  }
  
  public nextSlide() {
    this.slideshow.onSlide(1);
  }
  public lastSlide() {
    this.slideshow.onSlide(-1);
  }
  public selectSlideId(nb: (number | Number) ) {
    this.slideshow.goToSlide(nb);
  }
  clicked() {
    console.log('clicked');
    
  }
}
