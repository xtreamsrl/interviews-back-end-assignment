import { ApiProperty } from '@nestjs/swagger';
import { IsNotEmpty, IsNumber, IsString, IsUrl } from 'class-validator';

export class CategoryDto {
  @ApiProperty({ description: 'The name of the category' })
  @IsString()
  @IsNotEmpty()
  name: string;
}
