import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'PseudoPipe'})
export class PseudoPipe implements PipeTransform {

  transform(value: string, args?: any): any {
    if ( value ) { value = value.trim(); }
    const reg = new RegExp(/([-_•]){0,1}(\w{2,}([-_•]){0,1})*/g);
    const res = reg.exec(value);
    if (res && res.length > 0 && res[0] !== '') {
      return  res[0];
    }
    return null;
  }

}
