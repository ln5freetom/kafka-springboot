import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Product } from '../../models/product';
import { WishlistService } from '../../services/wishlist.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-wishlist',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishlistComponent implements OnInit {
  userId: number = 0;
  products: Product[] = [];

  constructor(
    private route: ActivatedRoute,
    private wishlistService: WishlistService
  ) {}

  ngOnInit(): void {
    this.userId = Number(this.route.snapshot.paramMap.get('userId'));
    this.loadWishlist();
  }

  loadWishlist(): void {
    this.wishlistService.getWishlist(this.userId).subscribe(products => {
      this.products = products;
    });
  }

  remove(productId: number): void {
    this.wishlistService.removeProduct(this.userId, productId).subscribe(() => {
      this.loadWishlist();
    });
  }

  clear(): void {
    if (confirm('Are you sure you want to clear your wishlist?')) {
      this.wishlistService.clear(this.userId).subscribe(() => {
        this.loadWishlist();
      });
    }
  }
}
