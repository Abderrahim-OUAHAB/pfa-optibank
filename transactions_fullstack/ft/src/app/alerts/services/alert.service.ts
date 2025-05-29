import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AlertService {
  private baseUrl = 'http://localhost:8087/alerts';
  constructor(private http: HttpClient, private router: Router) { }

  getAllAlerts() {
    return this.http.get(`${this.baseUrl}/`);
  }

  updateAlert(alertId: string, status: string) {
    return this.http.put(`${this.baseUrl}/update/${alertId}/${status}`, {});
  }

  getAlertsByAccount(accountId: string): Observable<any> {
  return this.http.get(`${this.baseUrl}/find/${accountId}`);
}
  deleteAlert(accountId: string) {
    return this.http.delete(`${this.baseUrl}/delete/${accountId}`);
  }
}
