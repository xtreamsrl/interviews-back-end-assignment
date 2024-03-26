import { Controller, Get, HttpException, HttpStatus } from '@nestjs/common';
import { DiscountsService } from './discounts.service';
import { Product } from '../models/product.model';

@Controller('discounts')
export class DiscountsController {
  constructor(private discountService: DiscountsService) {}

  @Get('/products')
  async getDiscountProducts(): Promise<Product[]> {
    const products = await this.discountService.getDiscountProducts();
    if (!products)
      throw new HttpException('Products not found', HttpStatus.NOT_FOUND);
    return products;
  }
}
