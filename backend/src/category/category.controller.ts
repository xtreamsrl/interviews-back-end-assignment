import { Controller, Post, Get, Body } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiResponse, ApiBody } from '@nestjs/swagger';
import { CategoryService } from './caegory.service';
import { CategoryDto } from './dto';

@ApiTags('Category')
@Controller('category')
export class CategoryController {
  constructor(private categoryService: CategoryService) {}

  @Post('addcategory')
  @ApiOperation({ summary: 'Add a new category' })
  @ApiBody({ type: CategoryDto })
  @ApiResponse({
    status: 201,
    description: 'The category has been successfully created.',
  })
  @ApiResponse({
    status: 400,
    description: 'The category has not been created.',
  })
  async addCategory(@Body() dto: CategoryDto) {
    return this.categoryService.addCategory(dto);
  }

  @Get('fetchallcategories')
  @ApiOperation({ summary: 'Fetch all categories' })
  @ApiResponse({ status: 200, description: 'Return all categories.' })
  fetchAllCategories() {
    return this.categoryService.fetchAllCategories();
  }
}
