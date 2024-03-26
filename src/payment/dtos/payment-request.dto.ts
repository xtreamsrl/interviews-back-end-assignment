import { IsNotEmpty, IsString } from 'class-validator';

export class PaymentRequestDto {
  @IsString()
  orderId: string;

  @IsNotEmpty()
  @IsString()
  cardNumber: string;

  @IsNotEmpty()
  @IsString()
  expiryMonth: string;

  @IsNotEmpty()
  @IsString()
  expiryYear: string;

  @IsNotEmpty()
  @IsString()
  cvv: string;

  amount: number;
}
