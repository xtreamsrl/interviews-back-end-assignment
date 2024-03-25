import { Order } from '../../models/order.model';
import { ProductsService } from '../../products/products.service';

export default async function calculateTotalPrice({
  order,
  productService,
}: {
  order: Order;
  productService: ProductsService;
}): Promise<number> {
  let totalPrice = 0;
  for (const item of order.products) {
    const product = await productService.getProductById(
      item.product_id.toString(),
    );
    totalPrice += product.price * item.quantity;
  }
  return totalPrice;
}
