import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private apiUrl = `http://localhost:5000/`;

  constructor(private http: HttpClient) { }

  initDocs(): Observable<any> {
    return this.http.post(`${this.apiUrl}/init`, {});
  }

  sendMessage(message: string, location: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/ask`, { message, location });
  }

  resetConversation(): Observable<any> {
    return this.http.post(`${this.apiUrl}/reset`, {});
  }
}