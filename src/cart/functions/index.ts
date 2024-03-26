import { Cart } from '../../models/cart.model';
import { ProductsService } from '../../products/products.service';

export default async function calculateTotalPrice({
  cart,
  productService,
}: {
  cart: Cart;
  productService: ProductsService;
}): Promise<number> {
  let totalPrice = 0;
  for (const item of cart.cartItems) {
    const product = await productService.getProductById(
      item.product._id.toString(),
    );

    totalPrice += product.price * item.quantity;
  }
  return totalPrice;
}
