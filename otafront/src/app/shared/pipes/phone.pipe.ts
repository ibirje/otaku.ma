import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'phone'
})
export class PhonePipe implements PipeTransform {

  transform(value: any, args?: any): any {
    if ( value ) { value = value.trim(); }
    const reg = new RegExp(/\+{0,1}(\d{1,4}[\s\-]{0,1})+\d{2,}/g);
    const res = reg.exec(value.trim());
    if (res && res.length > 0 && res[0] !== '') { return  res[0]; }
    return null;
  }

}
