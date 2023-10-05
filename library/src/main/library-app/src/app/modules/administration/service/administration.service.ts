import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment.development';

import { Observable } from 'rxjs';
import { AddBookData, BookResponse } from '../../core/models/add-book-managment';

@Injectable({
  providedIn: 'root'
})
export class AdministrationService {
  apiUrl = `${environment.apiUrl}/books`;

  constructor(
    private http: HttpClient
  ) { }

  addBook(addBookData: AddBookData): Observable<BookResponse> {
    return this.http.post<BookResponse>(`${this.apiUrl}`, addBookData, {
      withCredentials: true,
    })
  }
}
