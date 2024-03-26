import { IsNotEmpty, IsArray, ValidateNested, IsString } from 'class-validator';
import { Type } from 'class-transformer';
import { Product } from '../../models/product.model';

export class CreateCartDto {
  @IsNotEmpty()
  @IsString()
  name: string;

  @IsNotEmpty()
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => CartItemDto)
  cartItems: CartItemDto[];
}

export class CartItemDto {
  @IsNotEmpty()
  product: Product | any;

  @IsNotEmpty()
  quantity: number;
}

export type UpdateCartDTO = Partial<CreateCartDto>;
