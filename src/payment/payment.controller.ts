import { Body, Controller, Post, Req, UseGuards } from '@nestjs/common';
import { PaymentService } from './payment.service';
import { PaymentRequestDto } from './dtos/payment-request.dto';
import { AuthGuard } from '../guard/auth.guard';

interface PaymentRequest {
  paymentRequestDto?: PaymentRequestDto;
  orderId?: string;
}
@Controller('payment')
export class PaymentsController {
  constructor(private readonly paymentsService: PaymentService) {}

  @UseGuards(AuthGuard)
  @Post()
  async processPayment(
    @Body() paymentRequest: PaymentRequest,
    @Req() req: Request,
  ) {
    const user = req['user'];

    return this.paymentsService.processPayment(
      user.user._id,
      paymentRequest.orderId,
      paymentRequest.paymentRequestDto,
    );
  }
}
