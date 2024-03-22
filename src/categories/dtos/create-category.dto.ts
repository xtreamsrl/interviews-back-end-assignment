import { IsNotEmpty, IsString, IsOptional } from 'class-validator';

export class CreateCategoryDTO {
  @IsNotEmpty()
  @IsString()
  name: string;

  @IsString()
  @IsOptional()
  description?: string;
}

export type UpdateCategoryDTO = Partial<CreateCategoryDTO>;
