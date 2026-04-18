import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/product';

@Injectable({
  providedIn: 'root'
})
export class WishlistService {

  private apiUrl = '/api/wishlist';

  constructor(private http: HttpClient) { }

  getWishlist(userId: number): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/${userId}`);
  }

  addProduct(userId: number, productId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${userId}/products/${productId}`, null);
  }

  removeProduct(userId: number, productId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${userId}/products/${productId}`);
  }

  clear(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${userId}`);
  }
}
