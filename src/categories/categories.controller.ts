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
} from '@nestjs/common';
import { CategoriesService } from './categories.service';
import { Category } from '../models/category.model';
import mongoose from 'mongoose';
import {
  CreateCategoryDTO,
  UpdateCategoryDTO,
} from './dtos/create-category.dto';

@Controller('categories')
export class CategoriesController {
  constructor(private categoriesService: CategoriesService) {}

  @Get()
  async getCategories(): Promise<Category[]> {
    try {
      const categories = await this.categoriesService.getCategories();
      if (!categories)
        throw new HttpException('Categories not found', HttpStatus.NOT_FOUND);
      return categories;
    } catch (error) {
      if (error instanceof HttpException) throw error;
      throw new HttpException(error.message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Get(':id')
  async getCategoryById(@Param('id') id: string): Promise<Category> {
    try {
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
    } catch (error) {
      if (error instanceof HttpException) throw error;
      throw new HttpException(error.message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Post()
  async addCategories(
    @Body() createCategoryDTOs: CreateCategoryDTO | CreateCategoryDTO[],
  ) {
    try {
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
    } catch (error) {
      if (error instanceof HttpException) throw error;
      throw new HttpException(error.message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Put(':id')
  async updateCategory(
    @Param('id') id: string,
    @Body() updateCategoryDTO: UpdateCategoryDTO,
  ): Promise<Category> {
    try {
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
    } catch (error) {
      if (error instanceof HttpException) throw error;
      throw new HttpException(error.message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Delete(':id')
  async deleteCategory(@Param('id') id: string): Promise<Category> {
    try {
      const deletedCategory = await this.categoriesService.deleteCategory(id);
      if (!deletedCategory)
        throw new NotFoundException('Category does not exist!');

      return deletedCategory;
    } catch (error) {
      if (error instanceof HttpException) throw error;
      throw new HttpException(error.message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Delete()
  async deleteCategories(@Body() ids: string[]): Promise<Category[]> {
    try {
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
    } catch (error) {
      if (error instanceof HttpException) {
        throw error;
      }
      throw new HttpException(error.message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
