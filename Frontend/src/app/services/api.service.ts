import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private http: HttpClient) { }

  get(path: string, params?: HttpParams): Observable<any> {
    return this.http.get(`${environment.restApiUrl}${path}`, { params: params });
  }

  post(path: string, body: Object = {}): Observable<any> {
    return this.http.post(`${environment.restApiUrl}${path}`, JSON.stringify(body), { headers: this.setHeaders() });
  }

  put(path: string, body: Object = {}): Observable<any> {
    return this.http.put(`${environment.restApiUrl}${path}`, JSON.stringify(body), { headers: this.setHeaders() });
  }

  delete(path: string): Observable<any> {
    return this.http.delete(`${environment.restApiUrl}${path}`, {});
  }
}
