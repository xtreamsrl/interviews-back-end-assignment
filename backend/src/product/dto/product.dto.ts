import { IsNotEmpty, IsNumber, IsString, IsUrl } from 'class-validator';

export class ProductDto {
  @IsString()
  @IsNotEmpty()
  name: string;

  @IsString()
  @IsNotEmpty()
  serialNo: string;

  @IsUrl()
  @IsNotEmpty()
  imageURL: string;

  @IsNumber()
  @IsNotEmpty()
  price: number;

  @IsNumber()
  @IsNotEmpty()
  quantity: number;

  @IsNumber()
  @IsNotEmpty()
  categoryId: number;
}
