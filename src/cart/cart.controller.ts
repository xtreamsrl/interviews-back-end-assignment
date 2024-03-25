import {
  Body,
  Controller,
  Delete,
  HttpException,
  HttpStatus,
  Param,
  Post,
  Put,
} from '@nestjs/common';
import { CartService } from './cart.service';
import { ItemDto } from '../orders/dtos/create-order.dto';

@Controller('cart')
export class CartController {
  constructor(private cartService: CartService) {}

  @Post('add-item')
  async addProductToCart(@Body() productDto: ItemDto): Promise<any> {
    const cart = await this.cartService.addItemToCart(productDto);
    return cart;
  }

  @Delete('remove-item/:productId')
  async removeItemFromCart(
    @Param('productId') productId: string,
  ): Promise<any> {
    try {
      const updatedOrder = await this.cartService.removeItemFromCart(productId);
      return updatedOrder;
    } catch (error) {
      throw new HttpException(
        'Failed to remove item from cart',
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @Delete('decrement-item/:productId')
  async decrementItemFromCart(
    @Param('productId') productId: string,
  ): Promise<any> {
    try {
      const updatedOrder =
        await this.cartService.decrementItemFromCart(productId);
      return updatedOrder;
    } catch (error) {
      throw new HttpException(
        'Failed to decrement item quantity in cart',
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }
  @Put('increment-item/:productId')
  async incrementItemInCart(
    @Param('productId') productId: string,
  ): Promise<any> {
    try {
      const updatedOrder =
        await this.cartService.incrementItemInCart(productId);
      return updatedOrder;
    } catch (error) {
      throw new HttpException(
        'Failed to increment item quantity in cart',
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @Put('update-item/:productId/:quantity')
  async updateCartItem(
    @Param('productId') productId: string,
    @Param('quantity') quantity: number,
  ): Promise<any> {
    try {
      const updatedOrder = await this.cartService.updateCartItem(
        productId,
        quantity,
      );
      return updatedOrder;
    } catch (error) {
      throw new HttpException(
        'Failed to update item quantity in cart',
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }
  @Delete('clear-cart')
  async clearCart(): Promise<any> {
    try {
      const updatedOrder = await this.cartService.clearCart();
      return {
        message: 'Cart cleared successfully',
        order: updatedOrder,
      };
    } catch (error) {
      throw new HttpException(
        'Failed to clear cart',
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }
}
