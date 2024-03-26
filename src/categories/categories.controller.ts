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
  Res,
  UseGuards,
} from '@nestjs/common';
import { CategoriesService } from './categories.service';
import mongoose from 'mongoose';
import {
  CreateCategoryDTO,
  UpdateCategoryDTO,
} from './dtos/create-category.dto';
import { AuthGuard } from '../guard/auth.guard';
import { AdminGuard } from '../guard/admin.guard';
import { Response } from 'express';

@Controller('categories')
export class CategoriesController {
  constructor(private categoriesService: CategoriesService) {}

  @Get()
  async getCategories(
    @Query('page') page: number,
    @Query('limit') limit: number,
    @Res() res: Response,
  ) {
    const categories = await this.categoriesService.getCategories(page, limit);
    if (!categories)
      throw new HttpException('Categories not found', HttpStatus.NOT_FOUND);

    return res.status(HttpStatus.OK).json({
      message: 'category found',
      data: categories,
    });
  }

  @Get(':id')
  async getCategoryById(@Param('id') id: string, @Res() res: Response) {
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

    return res.status(HttpStatus.OK).json({
      message: 'category found',
      data: category,
    });
  }
  @UseGuards(AuthGuard, AdminGuard)
  @Post()
  async addCategories(
    @Body() createCategoryDTOs: CreateCategoryDTO | CreateCategoryDTO[],
    @Res() res: Response,
  ) {
    if (Array.isArray(createCategoryDTOs)) {
      const categories = await Promise.all(
        createCategoryDTOs.map(async (createCategoryDTO) => {
          const category =
            await this.categoriesService.addCategory(createCategoryDTO);
          return category;
        }),
      );
      return res.status(HttpStatus.OK).json({
        message: 'categories added successfully',
        data: categories,
      });
    } else {
      const category =
        await this.categoriesService.addCategory(createCategoryDTOs);

      return res.status(HttpStatus.OK).json({
        message: 'category added successfully',
        data: category,
      });
    }
  }
  @UseGuards(AuthGuard, AdminGuard)
  @Put(':id')
  async updateCategory(
    @Param('id') id: string,
    @Body() updateCategoryDTO: UpdateCategoryDTO,
    @Res() res: Response,
  ) {
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

    return res.status(HttpStatus.OK).json({
      message: 'category updated successfully',
      data: updatedCategory,
    });
  }
  @UseGuards(AuthGuard, AdminGuard)
  @Delete(':id')
  async deleteCategory(@Param('id') id: string, @Res() res: Response) {
    const deletedCategory = await this.categoriesService.deleteCategory(id);
    if (!deletedCategory)
      throw new NotFoundException('Category does not exist!');

    return res.status(HttpStatus.OK).json({
      message: 'category deleted successfully',
      data: deletedCategory,
    });
  }
  @UseGuards(AuthGuard, AdminGuard)
  @Delete()
  async deleteCategories(@Body() ids: string[], @Res() res: Response) {
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

    return res.status(HttpStatus.OK).json({
      message: 'categories deleted successfully',
      data: deletedCategories,
    });
  }

  @Get(':categoryId/products')
  async getProductsByCategory(
    @Param('categoryId') categoryId: string,
    @Res() res: Response,
  ) {
    const products =
      await this.categoriesService.getProductsByCategory(categoryId);

    return res.status(HttpStatus.OK).json({
      message: 'get products by category successfully',
      data: products,
    });
  }
}
