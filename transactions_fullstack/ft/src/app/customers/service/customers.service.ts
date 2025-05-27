import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class CustomersService {
  private baseUrl = 'http://localhost:8087/customers';
  constructor(private http: HttpClient, private router: Router) { }


  createCustomer(customer: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/create`, customer);
  }

  deleteByEmail(email: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/delete/${email}`);
  }
  updateCustomer(email: string, customer: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/update/${email}`, customer);
  } 

  findByCustomerId(email: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/find/${email}`);
  }
}
