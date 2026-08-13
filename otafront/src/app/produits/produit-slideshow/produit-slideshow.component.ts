import { Produit } from './../../data/Produit';
import { FullVariation } from './../../data/restdata/FullVariation';
import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { IImage } from 'ng-simple-slideshow';

@Component({
  selector: 'app-produit-slideshow',
  templateUrl: './produit-slideshow.component.html',
  styleUrls: ['./produit-slideshow.component.css']
})
export class ProduitSlideshowComponent implements OnInit {
  
  @ViewChild('slideshow') slideshow: any;
  @ViewChild('block') block: any;
  protected _produit: Produit;
  protected _variations: [FullVariation];
   imageUrls: (string | IImage)[];

  height: String;
  minHeight: String ;
  arrowSize: String = '30px';
  showArrows: Boolean = true;
  disableSwiping: Boolean = false;
  autoPlay: Boolean = true;
  autoPlayInterval: Number = 3333;
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

  constructor() {
  }

  ngOnInit() {
    this.height = this.block.nativeElement.offsetWidth;
  }
  onResize(event) {

    this.height = this.block.nativeElement.offsetWidth;
  }
  
  public get produit(): Produit {
    return this._produit;
  }
  @Input() public set produit(pr: Produit) {
    this._produit = pr;
    if ( this._produit ) {
      
      this.imageUrls = [this._produit.image1];

      if ( this._produit.image2 ) {
        this.imageUrls.push(this._produit.image2);
      }  
      if ( this._produit.image3 ) {
        
        this.imageUrls.push(this._produit.image3);
      }
    }
  }

  public get variations(): [FullVariation] {
    return this._variations;
  }  
  @Input() public set variations(vr: [FullVariation] ) {
    this._variations = vr;
    if (this._variations) {
      this._variations.forEach(variation => {
        if ( variation.variation && variation.variation.image) {
          
          this.imageUrls.push(variation.variation.image);
        }
      });
    }
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
  public selectSlideImg(str: string) {
    let imgofCode;
    for ( const variation of this.variations) {
      if (variation.variation.code === str ) {
        imgofCode = variation.variation.image;
      }
    }
    // console.log('code : ' + str + ' image : ' + imgofCode + ' index : ' + this.imageUrls.indexOf(imgofCode));
    // problem with slideshow, gotoslide breaks after few calls
    this.slideshow.goToSlide(this.imageUrls.indexOf(imgofCode));
  }
}

