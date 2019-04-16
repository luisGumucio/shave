import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';

@Injectable()
export class FileService {

  constructor(private http: HttpClient) {
  }

  getAll(page: number): Observable<any> {
    return this.http.get('//localhost:4000/files?page=' + page);
  }

  addFile(item, option, procces): Observable<any> {
    let body = new FormData();
    // Add file content to prepare the request
    body.append("file", item);
    body.append("option", option);
    body.append("process", procces);

    return this.http.post("//localhost:4000/files", body).pipe();
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