import { Injectable } from '@nestjs/common';
import { Order } from '../models/order.model';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { CreateOrderDto, UpdateOrderDTO } from './dtos/create-order.dto';

@Injectable()
export class OrdersService {
  constructor(
    @InjectModel(Order.name)
    private orderModel: Model<Order>,
  ) {}

  async getOrderById(id: string): Promise<Order> {
    const order = await this.orderModel.findById(id);
    return order;
  }

  async createOrder(createOrderDto: CreateOrderDto): Promise<Order> {
    const newOrder = new this.orderModel(createOrderDto);
    const createdOrder = await newOrder.save();
    return createdOrder;
  }

  async updateOrder(id: string, updateData: UpdateOrderDTO): Promise<Order> {
    const order = await this.orderModel.findByIdAndUpdate(id, updateData, {
      new: true,
    });
    return order;
  }
}
