import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ExchangeRate } from '../models/exchange-rate.model';

@Injectable({
  providedIn: 'root'
})
export class ExchangeRateService {
  private apiUrl = 'http://localhost:8087/exchangerates';

  constructor(private http: HttpClient) { }

  getAllRates(): Observable<ExchangeRate[]> {
    return this.http.get<ExchangeRate[]>(this.apiUrl+'/');
  }

  getRateById(id: string): Observable<ExchangeRate> {
    return this.http.get<ExchangeRate>(`${this.apiUrl}/${id}`);
  }

  createRate(rate: ExchangeRate): Observable<ExchangeRate> {
    return this.http.post<ExchangeRate>(this.apiUrl+'/create', rate);
  }

  updateRate(rate: ExchangeRate): Observable<ExchangeRate> {
    return this.http.put<ExchangeRate>(`${this.apiUrl}/update/${rate.rateId}`, rate);
  }

  deleteRate(rateId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${rateId}`);
  }

  getRateByCurrencies(from: string, to: string): Observable<ExchangeRate> {
    return this.http.get<ExchangeRate>(`${this.apiUrl}/convert?from=${from}&to=${to}`);
  }
}