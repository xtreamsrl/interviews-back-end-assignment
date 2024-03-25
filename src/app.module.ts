import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { ConfigModule } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';
import { ProductsModule } from './products/products.module';
import { CategoriesModule } from './categories/categories.module';
import { OrdersModule } from './orders/orders.module';
import { CartService } from './cart/cart.service';
import { CartController } from './cart/cart.controller';
import { CartModule } from './cart/cart.module';

@Module({
  imports: [
    ConfigModule.forRoot({
      envFilePath: ['.env'],
      isGlobal: true,
    }),

    MongooseModule.forRoot(process.env.MONGO_URI),
    ProductsModule,
    CategoriesModule,
    OrdersModule,
    CartModule,
  ],
  controllers: [AppController, CartController],
  providers: [AppService, CartService],
})
export class AppModule {}
