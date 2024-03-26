import { Module, forwardRef } from '@nestjs/common';
import { DiscountsController } from './discounts.controller';
import { DiscountsService } from './discounts.service';
import { MongooseModule } from '@nestjs/mongoose';
import { Discount, DiscountSchema } from '../models/discount.model';
import { ProductsModule } from '../products/products.module';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: Discount.name, schema: DiscountSchema },
    ]),
    forwardRef(() => ProductsModule),
  ],
  controllers: [DiscountsController],
  providers: [DiscountsService],
})
export class DiscountsModule {}
