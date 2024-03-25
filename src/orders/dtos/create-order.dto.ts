import {
  IsNotEmpty,
  IsNumber,
  IsString,
  IsArray,
  ValidateNested,
} from 'class-validator';
import { Type } from 'class-transformer';
import { Types } from 'mongoose';

export class ItemDto {
  @IsNotEmpty()
  product_id: Types.ObjectId;

  @IsNotEmpty()
  @IsNumber()
  quantity: number;
}

export class CreateOrderDto {
  @IsNotEmpty()
  @IsString()
  user_id: string;

  @IsNotEmpty()
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => ItemDto)
  products: ItemDto[];

  @IsNumber()
  total_price: number;

  @IsString()
  status?: string;
}

export type UpdateOrderDTO = Partial<CreateOrderDto>;
