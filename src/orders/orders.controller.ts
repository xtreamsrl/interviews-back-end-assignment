import {
  Body,
  Controller,
  Get,
  HttpException,
  HttpStatus,
  Param,
  Post,
  Req,
  Res,
  UseGuards,
} from '@nestjs/common';
import { OrdersService } from './orders.service';
import { CreateOrderDto } from './dtos/create-order.dto';
import { AuthGuard } from '../guard/auth.guard';
import { Request, Response } from 'express';
import mongoose from 'mongoose';

@Controller('orders')
export class OrdersController {
  constructor(private ordersService: OrdersService) {}

  @UseGuards(AuthGuard)
  @Get(':id')
  async getOrderById(
    @Param('id') id: string,
    @Req() req: Request,
    @Res() res: Response,
  ) {
    const isValid = mongoose.Types.ObjectId.isValid(id);
    if (!isValid)
      throw new HttpException(
        'Invalid mongoose Id, Order not found',
        HttpStatus.BAD_REQUEST,
      );
    const user = req['user'];
    const order = await this.ordersService.getOrderById(user.user._id, id);

    if (!order) {
      throw new HttpException('Order not found', HttpStatus.NOT_FOUND);
    }

    return res.status(HttpStatus.OK).json({
      message: 'get order successfully',
      data: order,
    });
  }

  @UseGuards(AuthGuard)
  @Post()
  async createOrder(
    @Body() createOrderDto: CreateOrderDto,
    @Req() req: Request,
    @Res() res: Response,
  ) {
    const isValid = mongoose.Types.ObjectId.isValid(createOrderDto.cart);
    if (!isValid)
      throw new HttpException(
        'Invalid mongoose Id, cart not found',
        HttpStatus.BAD_REQUEST,
      );

    const user = req['user'];

    const createdOrder = await this.ordersService.createOrder(
      user.user._id.toString(),
      createOrderDto,
    );

    return res.status(HttpStatus.OK).json({
      message: 'create order successfully',
      data: createdOrder,
    });
  }
}
