import {
  Body,
  Controller,
  Get,
  HttpException,
  HttpStatus,
  Param,
  Post,
  Put,
  UseGuards,
} from '@nestjs/common';
import { OrdersService } from './orders.service';
import { Order } from '../models/order.model';
import { CreateOrderDto, UpdateOrderDTO } from './dtos/create-order.dto';
import { AuthGuard } from '../guard/auth.guard';

@Controller('orders')
export class OrdersController {
  constructor(private ordersService: OrdersService) {}

  @Get(':id')
  async getOrderById(@Param('id') id: string): Promise<Order> {
    const order = await this.ordersService.getOrderById(id);
    if (!order) {
      throw new HttpException('Order not found', HttpStatus.NOT_FOUND);
    }
    return order;
  }

  @UseGuards(AuthGuard)
  @Post()
  async createOrder(@Body() createOrderDto: CreateOrderDto): Promise<Order> {
    try {
      const createdOrder = await this.ordersService.createOrder(createOrderDto);
      return createdOrder;
    } catch (error) {
      throw new HttpException(
        'Failed to create order',
        HttpStatus.INTERNAL_SERVER_ERROR,
      );
    }
  }

  @UseGuards(AuthGuard)
  @Put(':id')
  async updateOrder(
    @Param('id') id: string,
    @Body() updateOrderDTO: UpdateOrderDTO,
  ): Promise<Order> {
    const updatedOrder = await this.ordersService.updateOrder(
      id,
      updateOrderDTO,
    );
    if (!updatedOrder) {
      throw new HttpException('Order not found', HttpStatus.NOT_FOUND);
    }
    return updatedOrder;
  }
}
