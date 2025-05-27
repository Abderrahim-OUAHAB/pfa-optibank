import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Transaction } from '../models/transaction.model';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private apiUrl = `http://localhost:8087/transactions`;

  constructor(private http: HttpClient) {}

  createTransaction(transaction: Transaction): Observable<any> {
    return this.http.post(this.apiUrl+'/createTransaction', transaction);
  }

  getTransactionsByAccount(accountId: string): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.apiUrl}/account/${accountId}`);
  }

    getTransactionsByUserEmail(userEmail: string): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.apiUrl}/user/${userEmail}`);
  }

  getAllTransactions(): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.apiUrl}/`);
  }
}