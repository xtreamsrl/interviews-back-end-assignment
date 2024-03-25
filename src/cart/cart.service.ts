import { HttpException, HttpStatus, Injectable } from '@nestjs/common';
import { Order } from '../models/order.model';
import { OrdersService } from '../orders/orders.service';
import { ProductsService } from '../products/products.service';
import { ItemDto } from '../orders/dtos/create-order.dto';
import calculateTotalPrice from './functions';

@Injectable()
export class CartService {
  constructor(
    private readonly orderService: OrdersService,
    private readonly productService: ProductsService,
  ) {}

  async addItemToCart(itemDto: ItemDto): Promise<any> {
    const orderId = '66017292853ed44d15e30595';
    const order = await this.orderService.getOrderById(orderId);
    console.log(itemDto);
    const product = await this.productService.getProductById(
      itemDto.product_id.toString(),
    );
    if (!product)
      throw new HttpException('product not found', HttpStatus.NOT_FOUND);

    // Check if the product is already in the cart
    const existingProductIndex = order.products.findIndex(
      (item) => item.product_id.toString() === itemDto.product_id.toString(),
    );
    if (existingProductIndex !== -1) {
      // If the product is already in the cart,give an error
      throw new Error('Product is already exists on the cart');
    } else {
      // If the product is not in the cart, add a new item to the products list
      order.products.push({
        product_id: itemDto.product_id,
        quantity: itemDto.quantity || 1,
      });
    }
    const total_price: number = await calculateTotalPrice({
      order: order,
      productService: this.productService,
    });

    order.set({ total_price: total_price });
    const updatedOrder = await order.save();
    return updatedOrder;
  }

  async removeItemFromCart(productId: string): Promise<Order> {
    const orderId = '66017292853ed44d15e30595';

    const order = await this.orderService.getOrderById(orderId);

    // Find the index of the product in the cart
    const productIndex = order.products.findIndex(
      (item) => item.product_id.toString() === productId,
    );

    // If the product is not in the cart, throw an error
    if (productIndex === -1) {
      throw new HttpException(
        'Product not found in cart',
        HttpStatus.NOT_FOUND,
      );
    }

    // Remove the product from the cart
    order.products.splice(productIndex, 1);

    const total_price: number = await calculateTotalPrice({
      order: order,
      productService: this.productService,
    });
    order.set({ total_price: total_price });

    // Save the updated order back to the database
    const updatedOrder = await order.save();
    return updatedOrder;
  }

  async decrementItemFromCart(productId: string): Promise<Order> {
    const orderId = '66017292853ed44d15e30595';

    const order = await this.orderService.getOrderById(orderId);

    // Find the index of the product in the cart
    const productIndex = order.products.findIndex(
      (item) => item.product_id.toString() === productId,
    );

    // If the product is not in the cart, throw an error
    if (productIndex === -1) {
      throw new HttpException(
        'Product not found in cart',
        HttpStatus.NOT_FOUND,
      );
    }

    // Decrement the quantity of the product in the cart
    const product = order.products[productIndex];
    if (product.quantity > 1) {
      product.quantity--; // Decrement quantity by 1
    } else {
      // If the quantity is already 1, remove the product from the cart
      order.products.splice(productIndex, 1);
    }

    const total_price: number = await calculateTotalPrice({
      order: order,
      productService: this.productService,
    });
    order.set({ total_price: total_price });

    // Save the updated order back to the database
    const updatedOrder = await order.save();
    return updatedOrder;
  }

  async incrementItemInCart(productId: string): Promise<any> {
    const orderId = '66017292853ed44d15e30595';
    const order = await this.orderService.getOrderById(orderId);

    // Find the index of the product in the cart
    const productIndex = order.products.findIndex(
      (item) => item.product_id.toString() === productId,
    );

    // If the product is not in the cart, throw an error
    if (productIndex === -1) {
      throw new HttpException(
        'Product not found in cart',
        HttpStatus.NOT_FOUND,
      );
    }

    // Increment the quantity of the product in the cart
    const product = order.products[productIndex];
    product.quantity++; // Increment quantity by 1

    const total_price: number = await calculateTotalPrice({
      order: order,
      productService: this.productService,
    });

    order.set({ total_price: total_price });

    // Save the updated order back to the database
    const updatedOrder = await order.save();
    return updatedOrder;
  }

  async updateCartItem(productId: string, quantity: number): Promise<Order> {
    const orderId = '66017292853ed44d15e30595';
    const order = await this.orderService.getOrderById(orderId);

    // Find the index of the product in the cart
    const productIndex = order.products.findIndex(
      (item) => item.product_id.toString() === productId,
    );

    // If the product is not in the cart, throw an error
    if (productIndex === -1) {
      throw new HttpException(
        'Product not found in cart',
        HttpStatus.NOT_FOUND,
      );
    }

    // Update the quantity of the product in the cart
    order.products[productIndex].quantity = quantity;

    const total_price: number = await calculateTotalPrice({
      order: order,
      productService: this.productService,
    });
    order.set({ total_price: total_price });

    // Save the updated order back to the database
    const updatedOrder = await order.save();
    return updatedOrder;
  }
  async clearCart(): Promise<Order> {
    const orderId = '66017292853ed44d15e30595';

    const order = await this.orderService.getOrderById(orderId);

    // Clear all products from the cart
    order.products = [];

    order.set({ total_price: 0 });
    const updatedOrder = await order.save();
    return updatedOrder;
  }
}
