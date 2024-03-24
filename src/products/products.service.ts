import { HttpException, HttpStatus, Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Product } from 'src/models/product.model';
import { CreateProductDTO, UpdateProductDTO } from './dtos/create-product.dto';
import { Model, Types } from 'mongoose';
import { CategoriesService } from '../categories/categories.service';

@Injectable()
export class ProductsService {
  constructor(
    @InjectModel(Product.name)
    private productModel: Model<Product>,
    private readonly categoryService: CategoriesService,
  ) {}

  async getProducts(page?: number, limit?: number): Promise<Product[]> {
    if (page <= 0 || limit <= 0) {
      throw new HttpException(
        'Invalid page or limit value.',
        HttpStatus.BAD_REQUEST,
      );
    }
    const skip = (page - 1) * limit;
    const products = await this.productModel.find().skip(skip).limit(limit);
    return products;
  }
  async addProduct(createProductDTO: CreateProductDTO): Promise<Product> {
    const { category }: { category: Types.ObjectId } = createProductDTO;
    const foundCategory = await this.categoryService.getCategoryById(
      category.toString(),
    );
    if (category && !foundCategory)
      throw new HttpException('category not found', HttpStatus.NOT_FOUND);
    const newProduct = await this.productModel.create(createProductDTO);
    return newProduct.save();
  }

  async getFilteredProducts(
    queryParams: Record<string, any>,
  ): Promise<Product[]> {
    const fields = Object.keys(queryParams);

    console.log(fields);
    const query = {};
    fields.forEach((field) => {
      if (isNaN(queryParams[field]))
        query[field] = {
          $regex: new RegExp(queryParams[field]),
          $options: 'i',
        };
    });

    const products = await this.productModel.find(query);
    return products;
  }

  async getProductById(id: string): Promise<Product> {
    const product = await this.productModel.findById(id);
    return product;
  }

  async updateProduct(
    id: string,
    updateProductDTO: UpdateProductDTO,
  ): Promise<Product> {
    const product = await this.productModel.findByIdAndUpdate(
      id,
      updateProductDTO,
      { new: true },
    );

    return product;
  }

  async deleteProduct(id: string): Promise<Product> {
    const product = await this.productModel.findByIdAndDelete(id);
    return product;
  }
}
