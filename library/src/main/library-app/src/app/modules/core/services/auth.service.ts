import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginData, IUser, RegisterData, RegisterResponse, LogoutResponse, LoggedInResponse } from '../models/auth.model';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  apiUrl = `${environment.apiUrl}/auth`;

  constructor(
    private http: HttpClient
  ) { }

  login(body: LoginData): Observable<IUser> {
    return this.http.post<IUser>(`${this.apiUrl}/login`, body, {
      withCredentials: true,//ciasteczka
    })
  }

  register(body: RegisterData): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(`${this.apiUrl}/register`, body);
  }

  logout(): Observable<LogoutResponse> {
    return this.http.get<LogoutResponse>(`${this.apiUrl}/logout`, {
      withCredentials: true,
    });
  }

  isLoggedIn(): Observable<LoggedInResponse> {
    return this.http.get<LoggedInResponse>(`${this.apiUrl}/logged-in`, {
      withCredentials: true,
    })
  }
}