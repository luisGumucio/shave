import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
// import { Ufv } from '.././components/ufv';

@Injectable()
export class ItemService {

  constructor(private http: HttpClient) {
  }


  getAll(page: number): Observable<any> {
    return this.http.get('//localhost:4000/items?page=' + page);
  }
  getByItemId(id): Observable<any> {
    return this.http.get('//localhost:4000/items/item/' + id);
  }


  getByItem(id): Observable<any> {
    return this.http.get('//localhost:4000/transaction/' + id);
  }
  getByStore(id): Observable<any> {
    return this.http.get('//localhost:4000/items/store/' + id);
  }
  
  private handleError(error: HttpErrorResponse): Observable<any> {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    // return an observable with a user-facing error message
    return throwError(
      'Something bad happened; please try again later.');
  };
}