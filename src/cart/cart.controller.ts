import {
  Controller,
  Post,
  Body,
  HttpStatus,
  HttpException,
  Get,
  Param,
  Delete,
  Put,
  UseGuards,
  Req,
  Res,
} from '@nestjs/common';
import { CartService } from './cart.service';
import { Cart } from '../models/cart.model';
import { CreateCartDto, UpdateCartDTO } from './dtos/create-cart.dto';
import { AuthGuard } from '../guard/auth.guard';
import { Request, Response } from 'express';
import { AdminGuard } from '../guard/admin.guard';

@Controller('carts')
export class CartController {
  constructor(private cartService: CartService) {}

  @UseGuards(AuthGuard)
  @Get('user')
  async getCartsByUser(@Req() req: Request, @Res() res: Response) {
    const user = req['user'];
    const carts = await this.cartService.getCartsByUserId(user.user._id);

    return res.status(HttpStatus.OK).json({
      message: 'Carts retrieved successfully',
      data: carts,
    });
  }

  @UseGuards(AuthGuard, AdminGuard)
  @Get()
  async getAllCarts(@Res() res: Response) {
    const carts = await this.cartService.getAllCarts();
    return res.status(HttpStatus.OK).json({
      message: 'All carts retrieved successfully',
      data: carts,
    });
  }
  @UseGuards(AuthGuard)
  @Post()
  async createCart(
    @Body() CartItemDto: CreateCartDto,
    @Req() req: Request,
    @Res() res: Response,
  ) {
    const user = req['user'];
    const newCart: Cart = await this.cartService.createCart(
      user.user._id,
      CartItemDto,
    );

    return res.status(HttpStatus.OK).json({
      message: 'Cart created successfully',
      data: newCart,
    });
  }

  @UseGuards(AuthGuard)
  @Get(':id')
  async findCartById(
    @Param('id') cartId: string,
    @Req() req: Request,
    @Res() res: Response,
  ) {
    const user = req['user'];

    const cart = await this.cartService.findCartById(
      user.user._id.toString(),
      cartId,
    );
    if (!cart) {
      throw new HttpException('Cart not found', HttpStatus.NOT_FOUND);
    }

    return res.status(HttpStatus.OK).json({
      message: 'Cart found successfully',
      data: cart,
    });
  }

  @UseGuards(AuthGuard)
  @Delete(':id')
  async deleteCart(
    @Param('id') cartId: string,
    @Req() req: Request,
    @Res() res: Response,
  ) {
    const user = req['user'];

    const deletedCart = await this.cartService.deleteCart(
      user.user._id.toString(),
      cartId,
    );
    if (!deletedCart) {
      throw new HttpException('Cart not found', HttpStatus.NOT_FOUND);
    }

    return res.status(HttpStatus.OK).json({
      message: 'Cart deleted successfully',
      data: deletedCart,
    });
  }

  @UseGuards(AuthGuard)
  @Put(':id')
  async updateCart(
    @Param('id') cartId: string,
    @Body() updateCartDto: UpdateCartDTO,
    @Req() req: Request,
    @Res() res: Response,
  ) {
    const user = req['user'];
    const updatedCart = await this.cartService.updateCart(
      user.user._id.toString(),
      cartId,
      updateCartDto,
    );
    if (!updatedCart) {
      throw new HttpException('Cart not found', HttpStatus.NOT_FOUND);
    }

    return res.status(HttpStatus.OK).json({
      message: 'Cart updated successfully',
      data: updatedCart,
    });
  }
}
