import { HttpException, HttpStatus, Injectable } from '@nestjs/common';
import { PaymentRequestDto } from './dtos/payment-request.dto';
import { PaymentResponseDto } from './dtos/payment-response.dto';
import { OrdersService } from '../orders/orders.service';
import { UsersService } from '../users/users.service';

@Injectable()
export class PaymentService {
  constructor(
    private readonly orderService: OrdersService,
    private readonly userService: UsersService,
  ) {}

  async processPayment(
    userId,
    orderId,
    paymentRequestDto: PaymentRequestDto,
  ): Promise<PaymentResponseDto> {
    const order = await this.orderService.getOrderById(userId, orderId);
    if (!order)
      throw new HttpException('Order not found', HttpStatus.BAD_REQUEST);

    //call external payment api to process payment
    //for example: const response = await axios.post("https://payment-gateway.com/process-payment", paymentRequestDto);

    const isPaymentSuccessful = Math.random() < 0.8; //now i simulate payment status
    if (isPaymentSuccessful) {
      const transactionId = 'tx_123456789';
      const status = 'completed';

      await this.orderService.updateOrderStatus(orderId, 'pending');
      await this.userService.updateRewardPoints(
        userId,
        Math.trunc(order.totalPrice),
      );
      return { transactionId, status };
    } else {
      await this.orderService.updateOrderStatus(orderId, 'canceled');
      throw new HttpException(
        'Invalid request provided',
        HttpStatus.BAD_REQUEST,
      );
    }
  }
}
