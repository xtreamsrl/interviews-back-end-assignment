import { Product } from '../../models/product.model';

export interface ProductResponse {
  data: Product[];
  page?: number;
  totalItems?: number;
  totalPages?: number;
}
