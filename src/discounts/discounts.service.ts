import { Inject, Injectable, forwardRef } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Discount } from '../models/discount.model';
import { Model } from 'mongoose';
import { ProductsService } from '../products/products.service';

@Injectable()
export class DiscountsService {
  constructor(
    @InjectModel(Discount.name)
    private discountModel: Model<Discount>,

    @Inject(forwardRef(() => ProductsService))
    private readonly productService: ProductsService,
  ) {}

  async getDiscountProducts(): Promise<any[]> {
    // Trova tutti i documenti di sconto
    const discounts = await this.discountModel.find().populate('productId');
    return discounts;
  }
}
