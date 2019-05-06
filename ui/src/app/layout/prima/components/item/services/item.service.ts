import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';

@Injectable()
export class ItemService {

  receiveUfv: any;
  constructor(private http: HttpClient) {
    this.receiveUfv = new EventEmitter();
  }
  
  showComponent() {
    this.receiveUfv.emit();
  }

  getAllItems(page: number): Observable<any> {
    return this.http.get('//localhost:4000/items?page=' + page);
  }

  getItemInformation(): Observable<any> {
    return this.http.get('//localhost:4000/items/sum');
  }
}