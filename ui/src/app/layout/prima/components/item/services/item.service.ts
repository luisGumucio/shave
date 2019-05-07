import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';

@Injectable()
export class ItemService {

  receiveUfv: any;
  itemId: 0;
  constructor(private http: HttpClient) {
    this.receiveUfv = new EventEmitter();
  }
  
  showComponent(itemId: any) {
    this.itemId = itemId;
    this.receiveUfv.emit();
  }

  getItemId() {
    return this.itemId;
  }

  getAllItems(page: number): Observable<any> {
    return this.http.get('//localhost:4000/items?page=' + page);
  }

  getItemInformation(): Observable<any> {
    return this.http.get('//localhost:4000/items/sum');
  }

  getTransactionDetail(itemId: any): Observable<any> {
    return this.http.get('//localhost:4000/transaction/' + itemId);
  }
}