import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Category } from 'src/models/category.model';
import {
  CreateCategoryDTO,
  UpdateCategoryDTO,
} from './dtos/create-category.dto';

@Injectable()
export class CategoriesService {
  constructor(
    @InjectModel(Category.name)
    private readonly categoryModel: Model<Category>,
  ) {}

  async getCategories(): Promise<Category[]> {
    return this.categoryModel.find();
  }

  async addCategory(createCategoryDTO: CreateCategoryDTO): Promise<Category> {
    const newCategory = await this.categoryModel.create(createCategoryDTO);
    return newCategory.save();
  }

  async getCategoryById(id: string): Promise<Category> {
    const category = await this.categoryModel.findById(id);
    return category;
  }

  async updateCategory(
    id: string,
    updateCategoryDTO: UpdateCategoryDTO,
  ): Promise<Category> {
    const updatedCategory = await this.categoryModel.findByIdAndUpdate(
      id,
      updateCategoryDTO,
      { new: true },
    );
    return updatedCategory;
  }

  async deleteCategory(id: string): Promise<Category> {
    const category = await this.categoryModel.findByIdAndDelete(id);
    return category;
  }
}
