import {
  Body,
  Controller,
  Delete,
  Get,
  HttpException,
  HttpStatus,
  NotFoundException,
  Param,
  Post,
  Put,
  Query,
} from '@nestjs/common';
import { CategoriesService } from './categories.service';
import { Category } from '../models/category.model';
import mongoose from 'mongoose';
import {
  CreateCategoryDTO,
  UpdateCategoryDTO,
} from './dtos/create-category.dto';
import { Product } from '../models/product.model';

@Controller('categories')
export class CategoriesController {
  constructor(private categoriesService: CategoriesService) {}

  @Get()
  async getCategories(
    @Query('page') page: number,
    @Query('limit') limit: number,
  ): Promise<Category[]> {
    const categories = await this.categoriesService.getCategories(page, limit);
    if (!categories)
      throw new HttpException('Categories not found', HttpStatus.NOT_FOUND);
    return categories;
  }

  @Get(':id')
  async getCategoryById(@Param('id') id: string): Promise<Category> {
    const isValid = mongoose.Types.ObjectId.isValid(id);
    if (!isValid)
      throw new HttpException(
        'Invalid mongoose Id, Category not found',
        HttpStatus.NOT_FOUND,
      );
    const category = await this.categoriesService.getCategoryById(id);
    if (!category) {
      throw new HttpException('Category not found', HttpStatus.NOT_FOUND);
    }
    return category;
  }

  @Post()
  async addCategories(
    @Body() createCategoryDTOs: CreateCategoryDTO | CreateCategoryDTO[],
  ) {
    if (Array.isArray(createCategoryDTOs)) {
      const categories = await Promise.all(
        createCategoryDTOs.map(async (createCategoryDTO) => {
          const category =
            await this.categoriesService.addCategory(createCategoryDTO);
          return category;
        }),
      );
      return categories;
    } else {
      const category =
        await this.categoriesService.addCategory(createCategoryDTOs);
      return category;
    }
  }

  @Put(':id')
  async updateCategory(
    @Param('id') id: string,
    @Body() updateCategoryDTO: UpdateCategoryDTO,
  ): Promise<Category> {
    const isValid = mongoose.Types.ObjectId.isValid(id);
    if (!isValid)
      throw new HttpException(
        'Invalid mongoose Id, Category not found to update',
        HttpStatus.NOT_FOUND,
      );
    const updatedCategory = await this.categoriesService.updateCategory(
      id,
      updateCategoryDTO,
    );
    if (!updatedCategory) {
      throw new NotFoundException('Category does not exist!');
    }
    return updatedCategory;
  }

  @Delete(':id')
  async deleteCategory(@Param('id') id: string): Promise<Category> {
    const deletedCategory = await this.categoriesService.deleteCategory(id);
    if (!deletedCategory)
      throw new NotFoundException('Category does not exist!');

    return deletedCategory;
  }

  @Delete()
  async deleteCategories(@Body() ids: string[]): Promise<Category[]> {
    const deletedCategories = [];
    for (const id of ids) {
      const deletedCategory = await this.categoriesService.deleteCategory(id);
      if (!deletedCategory) {
        throw new HttpException(
          `Category with ID ${id} does not exist!`,
          HttpStatus.NOT_FOUND,
        );
      }
      deletedCategories.push(deletedCategory);
    }
    return deletedCategories;
  }

  @Get(':categoryId/products')
  async getProductsByCategory(
    @Param('categoryId') categoryId: string,
    @Query('page') page: number,
    @Query('limit') limit: number,
  ): Promise<Product[]> {
    const products = await this.categoriesService.getProductsByCategory(
      categoryId,
      page,
      limit,
    );
    return products;
  }
}
