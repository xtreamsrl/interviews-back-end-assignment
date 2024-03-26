import {
  Controller,
  Get,
  HttpException,
  HttpStatus,
  Res,
} from '@nestjs/common';
import { DiscountsService } from './discounts.service';
import { Response } from 'express';

@Controller('discounts')
export class DiscountsController {
  constructor(private discountService: DiscountsService) {}

  @Get('/products')
  async getDiscountProducts(@Res() res: Response) {
    const products = await this.discountService.getDiscountProducts();
    if (!products)
      throw new HttpException('Products not found', HttpStatus.NOT_FOUND);
    return res.status(HttpStatus.OK).json({
      message: 'get products successfully',
      data: products,
    });
  }
}
