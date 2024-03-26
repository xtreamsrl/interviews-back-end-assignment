import { IsNotEmpty, IsNumber, IsString, IsOptional } from 'class-validator';

export class CreateProductDTO {
  @IsNotEmpty()
  @IsString()
  name: string;

  @IsString()
  @IsOptional()
  description?: string;

  @IsString()
  @IsOptional()
  image?: string;

  @IsNotEmpty()
  @IsNumber()
  price: number;

  @IsNotEmpty()
  @IsNumber()
  availableQuantity: number;

  @IsNotEmpty()
  category: string;
}

export type UpdateProductDTO = Partial<CreateProductDTO>;
