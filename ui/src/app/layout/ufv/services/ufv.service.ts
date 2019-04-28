import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';

@Injectable()
export class UfvService {

  receiveUfv: any;
  constructor(private http: HttpClient) {
    this.receiveUfv = new EventEmitter();
  }

  getAll(page: number): Observable<any> {
    return this.http.get('//localhost:4000/ufvs?page=' + page);
  }
  
  addUfvFile(item): Observable<any> {
    let body = new FormData();
    // Add file content to prepare the request
    body.append("file", item);
    return this.http.post("//localhost:4000/files/ufv", body).pipe();
  }

  executeUfv(): void {
    this.receiveUfv.emit();
  }
}