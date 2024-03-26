import { Module } from '@nestjs/common';
import { PaymentsController } from './payment.controller';
import { PaymentService } from './payment.service';
import { OrdersModule } from '../orders/orders.module';
import { UsersModule } from '../users/users.module';

@Module({
  imports: [OrdersModule, UsersModule],
  controllers: [PaymentsController],
  providers: [PaymentService],
})
export class PaymentModule {}
