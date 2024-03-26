import {
  Injectable,
  NotFoundException,
  UnauthorizedException,
} from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Cart } from '../models/cart.model';
import { CreateCartDto, UpdateCartDTO } from './dtos/create-cart.dto';
import calculateTotalPrice from './functions';
import { ProductsService } from '../products/products.service';

@Injectable()
export class CartService {
  constructor(
    @InjectModel(Cart.name) private readonly cartModel: Model<Cart>,
    private readonly productService: ProductsService,
  ) {}

  async createCart(
    userId: string,
    createCartDto: CreateCartDto,
  ): Promise<Cart> {
    const newCart = await this.cartModel.create({
      user: userId,
      cartItems: createCartDto.cartItems,
      name: createCartDto.name,
    });
    const total = await calculateTotalPrice({
      cart: newCart,
      productService: this.productService,
    });

    newCart.set({ total });
    return newCart.save();
  }

  async findCartById(userId: string, cartId: string): Promise<Cart> {
    const cart = await this.cartModel.findById(cartId);
    if (cart && cart.user.toString() !== userId)
      throw new UnauthorizedException(
        'You are not authorized to get this cart',
      );
    return cart;
  }

  async getCartsByUserId(userId: string): Promise<Cart[]> {
    const carts = await this.cartModel.find({ user: userId });
    return carts;
  }

  async getAllCarts(): Promise<Cart[]> {
    const carts = await this.cartModel.find();
    return carts;
  }

  async deleteCart(userId: string, cartId: string): Promise<Cart> {
    const cart = await this.cartModel.findById(cartId);
    if (cart && cart.user.toString() !== userId)
      throw new UnauthorizedException(
        'You are not authorized to delete this cart',
      );

    const deletedCart = await this.cartModel.findByIdAndDelete(cartId);
    return deletedCart;
  }

  async updateCart(
    userId: string,
    cartId: string,
    updateCartDto: UpdateCartDTO,
  ): Promise<Cart> {
    const existingCart = await this.cartModel.findById(cartId);

    if (existingCart && existingCart.user.toString() !== userId)
      throw new UnauthorizedException(
        'You are not authorized to update this cart',
      );

    if (!existingCart) {
      throw new NotFoundException('Cart not found');
    }

    if (updateCartDto.name !== existingCart.name)
      existingCart.set({ name: updateCartDto.name });

    if (updateCartDto.cartItems && updateCartDto.cartItems.length > 0) {
      updateCartDto.cartItems.forEach((updateCartItem) => {
        const existingCartItemIndex = existingCart.cartItems.findIndex(
          (item) =>
            item.product.toString() === updateCartItem.product.toString(),
        );

        if (existingCartItemIndex !== -1) {
          // if element already exists, update it
          if (updateCartItem.quantity === 0) {
            // if the quantity equals to 0, remove the item from the cart
            existingCart.cartItems.splice(existingCartItemIndex, 1);
          } else {
            // update the quantity
            existingCart.cartItems[existingCartItemIndex].quantity =
              updateCartItem.quantity;
          }
        } else {
          //create new cart item
          existingCart.cartItems.push(updateCartItem);
        }
      });
    }

    const total = await calculateTotalPrice({
      cart: existingCart,
      productService: this.productService,
    });

    existingCart.set({ total });
    return existingCart.save();
  }
}
