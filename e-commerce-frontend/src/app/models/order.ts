import { User } from './user';

export interface Order {
  id: number;
  orderNumber: string;
  status: string;
  totalAmount: number;
  user: User;
  createdAt: string;
}
