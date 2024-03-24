import {
  HttpException,
  HttpStatus,
  Inject,
  Injectable,
  forwardRef,
} from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Category } from 'src/models/category.model';
import {
  CreateCategoryDTO,
  UpdateCategoryDTO,
} from './dtos/create-category.dto';
import { Product } from '../models/product.model';
import { ProductsService } from '../products/products.service';

@Injectable()
export class CategoriesService {
  constructor(
    @InjectModel(Category.name)
    private readonly categoryModel: Model<Category>,

    @Inject(forwardRef(() => ProductsService))
    private readonly productService: ProductsService,
  ) {}

  async getCategories(page?: number, limit?: number): Promise<Category[]> {
    if (page <= 0 || limit <= 0)
      throw new HttpException(
        'Invalid page or limit value.',
        HttpStatus.BAD_REQUEST,
      );
    const skip = (page - 1) * limit;
    return this.categoryModel.find().skip(skip).limit(limit);
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

  async getProductsByCategory(categoryId: string): Promise<Product[]> {
    const products = await this.productService.getFilteredProducts({
      category: categoryId,
    });
    return products;
  }
}
