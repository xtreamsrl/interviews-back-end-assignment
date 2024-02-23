import { IsNotEmpty, IsNumber, IsString, IsUrl } from 'class-validator';
import { ApiProperty } from '@nestjs/swagger';

export class ProductDto {
  @ApiProperty({ description: 'The name of the product' })
  @IsString()
  @IsNotEmpty()
  name: string;

  @ApiProperty({ description: 'The serial number of the product' })
  @IsString()
  @IsNotEmpty()
  serialNo: string;

  @ApiProperty({ description: 'The image URL of the product' })
  @IsUrl()
  @IsNotEmpty()
  imageURL: string;

  @ApiProperty({ description: 'The price of the product' })
  @IsNumber()
  @IsNotEmpty()
  price: number;

  @ApiProperty({ description: 'The available quantity of the product' })
  @IsNumber()
  @IsNotEmpty()
  quantity: number;

  @ApiProperty({ description: 'The category ID of the product' })
  @IsNumber()
  @IsNotEmpty()
  categoryId: number;
}
