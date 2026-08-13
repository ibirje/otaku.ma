import { SlideshowModule } from 'ng-simple-slideshow';
import { TypeDropdownComponent } from './../header/type-dropdown/type-dropdown.component';
import { DropdownComponent } from './../header/dropdown/dropdown.component';
import { RangeSliderModule } from 'ngx-rangeslider-component';
import { FormsModule } from '@angular/forms';
import { ItemProduitComponent } from './../produits/item-produit/item-produit.component';
import { ProduitListeComponent } from './../produits/produit-liste/produit-liste.component';
import { SearchbarComponent } from './../header/searchbar/searchbar.component';
import { PopupComponent } from './popup/popup.component';
import { HeaderComponent } from './../header/header.component';
import { CountdownComponent } from './countdown/countdown.component';
import { EtoilesComponent } from './etoiles/etoiles.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PaginationComponent } from './pagination/pagination.component';
import { ListFiltresComponent } from './list-filtres/list-filtres.component';
import { RangesliderComponent } from './rangeslider/rangeslider.component';
import { SpinnerComponent } from './spinner/spinner.component';
import { SubtypeDropdownComponent } from '../header/subtype-dropdown/subtype-dropdown.component';
import { InfoBoxComponent } from './info-box/info-box.component';
import { BlockComponent } from './block/block.component';
import { PseudoPipe } from './pipes/pseudo.pipe';
import { NomPipe } from './pipes/nom.pipe';
import { PhonePipe } from './pipes/phone.pipe';
import { AdressePipe } from './pipes/adresse.pipe';
import { InputComponent } from './input/input.component';
import { ConfirmPopupComponent } from './confirm-popup/confirm-popup.component';
import { TypeListComponent } from './type-list/type-list.component';
import { TypeItemComponent } from './type-list/type-item/type-item.component';
import { SearchpopupComponent } from '../header/searchpopup/searchpopup.component';
import { CarouselComponent } from './carousel/carousel.component';
@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    RangeSliderModule,
    SlideshowModule
  ],
  
  declarations: [
    ProduitListeComponent,
    ItemProduitComponent,
    EtoilesComponent,
    CountdownComponent,
    HeaderComponent,
    PopupComponent,
    DropdownComponent,
    TypeDropdownComponent,
    SubtypeDropdownComponent,
    SearchbarComponent,
    SearchpopupComponent,
    PaginationComponent,
    ListFiltresComponent,
    RangesliderComponent,
    SpinnerComponent,
    InfoBoxComponent,
    BlockComponent,
    InputComponent,
    PseudoPipe,
    NomPipe,
    PhonePipe,
    AdressePipe,
    ConfirmPopupComponent,
    TypeListComponent,
    TypeItemComponent,
    CarouselComponent
  ],
  exports:      [
    ProduitListeComponent,
    ItemProduitComponent,
    EtoilesComponent,
    CountdownComponent,
    HeaderComponent,
    PopupComponent,
    DropdownComponent,
    TypeDropdownComponent,
    SubtypeDropdownComponent,
    SearchbarComponent ,
    SearchpopupComponent,
    PaginationComponent,
    ListFiltresComponent,
    RangesliderComponent,
    SpinnerComponent,
    InfoBoxComponent,
    BlockComponent,
    InputComponent,
    PseudoPipe,
    NomPipe,
    PhonePipe,
    AdressePipe,
    ConfirmPopupComponent,
    TypeListComponent,
    TypeItemComponent,
    CarouselComponent
  ]
})
export class SharedModule { }
