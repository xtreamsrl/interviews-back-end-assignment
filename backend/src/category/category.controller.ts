import { Controller, Post, Get, Body } from '@nestjs/common';
import { CategoryService } from './caegory.service';
import { CategoryDto } from './dto';

@Controller('category')
export class CategoryController {
  constructor(private categoryService: CategoryService) {}

  @Post('addcategory')
  addProduct(@Body() dto: CategoryDto) {
    return this.categoryService.addCategory(dto)
  }

  // TODO: iplement
  @Get('fetchallcategories')
  fetchAllProducts() {
    return this.categoryService.fetchAllCategories()
  }
}
