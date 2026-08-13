import { FormsModule } from '@angular/forms';
import { ThemesRoutingModule } from './themes-routing.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ThemesComponent } from './pages/themes/themes.component';
import { ThemeListComponent } from './pages/theme-list/theme-list.component';
import { SharedModule } from '../shared/shared.module';
import { RangeSliderModule } from 'ngx-rangeslider-component';

@NgModule({
  declarations: [ThemesComponent, ThemeListComponent],
  imports: [
    CommonModule,
    SharedModule,
    ThemesRoutingModule,
    FormsModule,
    RangeSliderModule
  ]
})
export class ThemesModule { }
