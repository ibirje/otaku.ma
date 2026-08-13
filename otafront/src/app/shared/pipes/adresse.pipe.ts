import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'adresse'
})
export class AdressePipe implements PipeTransform {

  transform(value: any, args?: any): any {
    if ( value ) { value = value.trim(); }
    const reg = new RegExp(/(\w*\s*([,.:]){0,1}\s*)*/g);
    const res = reg.exec(value);
    if (res && res.length > 0 && res[0] !== '') {
      return  res[0];
    }
    return null;
  }

}
