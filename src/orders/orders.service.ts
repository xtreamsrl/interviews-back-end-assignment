import {
  HttpException,
  HttpStatus,
  Injectable,
  UnauthorizedException,
} from '@nestjs/common';
import { Order } from '../models/order.model';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { CreateOrderDto } from './dtos/create-order.dto';
import { CartService } from '../cart/cart.service';

@Injectable()
export class OrdersService {
  constructor(
    @InjectModel(Order.name)
    private orderModel: Model<Order>,
    private readonly cartService: CartService,
  ) {}

  async getOrderById(userId: string, id: string): Promise<Order> {
    const order = await this.orderModel.findById(id);

    if (order && order.user.toString() !== userId)
      throw new UnauthorizedException(
        'You are not authorized to get this order',
      );
    return order;
  }

  async updateOrderStatus(orderId: string, newStatus: string): Promise<any> {
    const order = await this.orderModel.findByIdAndUpdate(
      orderId,
      {
        status: newStatus,
      },
      {
        new: true,
      },
    );
    return order;
  }

  async createOrder(
    userId: string,
    createOrderDto: CreateOrderDto,
  ): Promise<Order> {
    const cart = await this.cartService.findCartById(
      userId,
      createOrderDto.cart,
    );

    if (!cart)
      throw new HttpException('Cart not found', HttpStatus.BAD_REQUEST);

    const newOrder = new this.orderModel({
      ...createOrderDto,
      totalPrice: cart.total,
      user: userId,
    });

    const createdOrder = await newOrder.save();
    return createdOrder;
  }
}
