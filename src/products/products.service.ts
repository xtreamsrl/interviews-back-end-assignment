import { HttpException, HttpStatus, Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Product } from 'src/models/product.model';
import { CreateProductDTO, UpdateProductDTO } from './dtos/create-product.dto';
import { Model, Types } from 'mongoose';
import { Category } from '../models/category.model';

@Injectable()
export class ProductsService {
  constructor(
    @InjectModel(Product.name)
    private productModel: Model<Product>,
    @InjectModel(Category.name) private categoryModel: Model<Category>,
  ) {}

  async getProducts(): Promise<Product[]> {
    const products = await this.productModel.find();
    return products;
  }
  async addProduct(createProductDTO: CreateProductDTO): Promise<Product> {
    const { category }: { category: Types.ObjectId } = createProductDTO;
    const foundCategory = await this.categoryModel.findById(category);
    if (category && !foundCategory)
      throw new HttpException('category not found', HttpStatus.NOT_FOUND);
    const newProduct = await this.productModel.create(createProductDTO);
    return newProduct.save();
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
