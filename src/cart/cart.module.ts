import { Module } from '@nestjs/common';
import { CartController } from './cart.controller';
import { CartService } from './cart.service';
import { OrdersModule } from '../orders/orders.module';
import { ProductsModule } from '../products/products.module';

@Module({
  imports: [OrdersModule, ProductsModule],
  controllers: [CartController],
  providers: [CartService],
})
export class CartModule {}
