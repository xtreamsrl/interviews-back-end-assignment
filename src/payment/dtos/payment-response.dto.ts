import { IsEnum, IsNotEmpty, IsString } from 'class-validator';

export class PaymentResponseDto {
  @IsNotEmpty()
  @IsString()
  transactionId: string;

  @IsNotEmpty()
  @IsEnum(['approved', 'declined', 'error'])
  status: string;
}
