import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CardService {
private apiUrl = 'http://localhost:8087/cards';
  constructor(private http: HttpClient, private router: Router) { }

  createCard(card: any): Observable<any> {
    return this.http.post(this.apiUrl+'/create', card);
  }

  getCardByAccountId(accountId: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/find/${accountId}`);
  }
  deleteCardByAccountId(accountId: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${accountId}`);
  }

}
