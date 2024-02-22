import { IsNotEmpty, IsNumber, IsString, IsUrl } from "class-validator";

export class CategoryDto {
  @IsString()
  @IsNotEmpty()
  name: string;
}
