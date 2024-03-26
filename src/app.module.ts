import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';
import { ProductsModule } from './products/products.module';
import { CategoriesModule } from './categories/categories.module';
import { OrdersModule } from './orders/orders.module';
import { AuthModule } from './auth/auth.module';
import { UsersModule } from './users/users.module';
import { CartModule } from './cart/cart.module';
import { PaymentModule } from './payment/payment.module';
import { DiscountsModule } from './discounts/discounts.module';
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
    AuthModule,
    UsersModule,
    CartModule,
    PaymentModule,
    DiscountsModule,
  ],
})
export class AppModule {}
