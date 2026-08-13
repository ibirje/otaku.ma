import { Component, OnInit, Input, forwardRef, ViewChild } from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';

@Component({
  selector: 'app-input',
  templateUrl: './input.component.html',
  styleUrls: ['./input.component.css'],
  providers: [{ provide: NG_VALUE_ACCESSOR, useExisting: forwardRef(() => InputComponent), multi: true }]
})
export class InputComponent implements ControlValueAccessor {

  @ViewChild('vinput') vinput: any;
  @Input() expanded = true;
  private _type = 'text';
  value = '';
  @Input() titre = '';

  onChange: (_: any) => void = (_: any) => {};
  onTouched: () => void = () => {};
  updateChanges() { this.onChange(this.value); }
  writeValue(value: string): void { this.value = value; this.updateChanges(); }
  registerOnChange(fn: any): void { this.onChange = fn; }
  registerOnTouched(fn: any): void { this.onTouched = fn; }

  @Input() get type() { return this._type; }
  set type(t) { this._type = t && t !== '' ? t : 'text'; }

  focusInput() {
    this.vinput.nativeElement.focus();
  }
  constructor() { }


}
