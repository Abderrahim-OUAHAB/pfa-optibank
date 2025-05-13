import { Injectable } from '@angular/core';
import { RegisterRequest } from '../models/register-request.model';
import { LoginRequest } from '../models/login-request.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8087/auth';
  private authStatus = new BehaviorSubject<{isAuthenticated: boolean, role: string}>({
    isAuthenticated: this.hasToken(),
    role: this.getUserRole()
  });
  authStatus$ = this.authStatus.asObservable();

  constructor(private http: HttpClient, private router: Router) {}

  private hasToken(): boolean {
    return !!localStorage.getItem('token');
  }

  private getUserRole(): string {
    return localStorage.getItem('role') || '';
  }

  login(data: {email: string, password: string, type: string}): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, data).pipe(
      tap((response: any) => {
        if (response.token) {
          localStorage.setItem('token', response.token);
          localStorage.setItem('role', response.role);
          this.authStatus.next({
            isAuthenticated: true,
            role: response.role
          });
        }
      })
    );
  }

  register(data: RegisterRequest): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, data);
  }

  logout(token: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.post(`${this.baseUrl}/logout`, {}, { headers }).pipe(
      tap(() => {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        this.authStatus.next({
            isAuthenticated: false,
            role:''
          });
        this.router.navigate(['/']);
      })
    );
  }

  
  getToken(): string | null {
    return localStorage.getItem('token');
  }
}