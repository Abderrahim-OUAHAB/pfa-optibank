import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private baseUrl = 'http://localhost:8087/accounts';
  constructor(private http: HttpClient, private router: Router) { }


  createAccount(account: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/create`, account);
  }
  deleteByCustomerId(customerId: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/delete/${customerId}`);
  }

  findAccountsByCustomerId(customerId: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/find/${customerId}`);
  }

  updateBalance(accountId: string, balance: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/update/${accountId}/${balance}`, {});
  }

  getAllAccounts(): Observable<any> {
    return this.http.get(`${this.baseUrl}/`);
  }
}
