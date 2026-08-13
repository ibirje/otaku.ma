import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'nom'
})
export class NomPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    if ( value ) { value = value.trim(); }
    const reg = new RegExp(/([a-zA-Zé]+(\'|\s){0,1})*/g);
    const res = reg.exec(value.trim());
    if (res && res.length > 0 && res[0] !== '') { return  res[0]; }
    return null;
  }

}
