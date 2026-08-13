import { Meta } from '@angular/platform-browser';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SeoService {

  constructor(private meta: Meta) { }
  generateTags(config) {
    config =  {
      title : 'BOBOBOBO',
      description : 'Plein de goodies et accessoires de jeux video et de mangas sur Otaku.ma',
      image: 'http://images2.imgbox.com/06/1c/qMBfnh3M_o.jpg',
      ...config
    };
    this.meta.updateTag({name : 'twitter:card', content : 'AJEEEEEEEEEEEEBEEEEEEEEEEEEEEEEEEEEEEEEEEE'});
    this.meta.updateTag({name : 'twitter:site', content : '@Otakuma'});
  }
  updateOG(name, ct) {
    
    
    this.meta.updateTag({property : 'og:' + name, content : ct});
  }
}
