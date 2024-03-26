import { IsNotEmpty, IsNumber, IsString, IsDate } from 'class-validator';

export class CreateDiscountDTO {
  @IsNotEmpty()
  @IsString()
  productId: string;

  @IsNotEmpty()
  @IsNumber()
  percentage: number;

  @IsNotEmpty()
  @IsDate()
  startDate: Date;

  @IsNotEmpty()
  @IsDate()
  endDate: Date;
}
export type UpdateDiscountDTO = Partial<CreateDiscountDTO>;
