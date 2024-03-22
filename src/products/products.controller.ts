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
import { ProductsService } from './products.service';
import { Product } from '../models/product.model';
import { CreateProductDTO, UpdateProductDTO } from './dtos/create-product.dto';
import mongoose from 'mongoose';

@Controller('products')
export class ProductsController {
  constructor(private productsService: ProductsService) {}

  @Get()
  async getProducts(): Promise<Product[]> {
    try {
      const products = await this.productsService.getProducts();
      if (!products)
        throw new HttpException('Products not found', HttpStatus.NOT_FOUND);
      return products;
    } catch (error) {
      if (error instanceof HttpException) throw error;
      throw new HttpException(error.message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Get(':id')
  async getProductById(@Param('id') id: string): Promise<Product> {
    try {
      const isValid = mongoose.Types.ObjectId.isValid(id);
      if (!isValid)
        throw new HttpException(
          'Invalid mongoose Id, Product not found',
          HttpStatus.NOT_FOUND,
        );
      const product = await this.productsService.getProductById(id);
      if (!product) {
        throw new HttpException('Product not found', HttpStatus.NOT_FOUND);
      }
      return product;
    } catch (error) {
      if (error instanceof HttpException) throw error;
      throw new HttpException(error.message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Post()
  async addProduct(@Body() createProductDTO: CreateProductDTO) {
    try {
      const product = await this.productsService.addProduct(createProductDTO);
      return product;
    } catch (error) {
      if (error instanceof HttpException) throw error;
      throw new HttpException(error.message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Put(':id')
  async updateProduct(
    @Param('id') id: string,
    @Body() updateProductDTO: UpdateProductDTO,
  ): Promise<Product> {
    try {
      const isValid = mongoose.Types.ObjectId.isValid(id);
      if (!isValid)
        throw new HttpException(
          'Invalid mongoose Id, Product not found to update',
          HttpStatus.NOT_FOUND,
        );
      const updatedProduct = await this.productsService.updateProduct(
        id,
        updateProductDTO,
      );
      if (!updatedProduct) {
        throw new NotFoundException('Product does not exist!');
      }
      return updatedProduct;
    } catch (error) {
      if (error instanceof HttpException) throw error;
      throw new HttpException(error.message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Delete(':id')
  async deleteProduct(@Param('id') id: string): Promise<Product> {
    try {
      const deletedProduct = await this.productsService.deleteProduct(id);
      if (!deletedProduct)
        throw new NotFoundException('Product does not exist!');

      return deletedProduct;
    } catch (error) {
      if (error instanceof HttpException) throw error;
      throw new HttpException(error.message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Delete()
  async deleteProducts(@Body() ids: string[]): Promise<Product[]> {
    try {
      const deletedProducts = [];
      for (const id of ids) {
        const deletedProduct = await this.productsService.deleteProduct(id);
        if (!deletedProduct) {
          throw new HttpException(
            `Product with ID ${id} does not exist!`,
            HttpStatus.NOT_FOUND,
          );
        }
        deletedProducts.push(deletedProduct);
      }
      return deletedProducts;
    } catch (error) {
      if (error instanceof HttpException) {
        throw error;
      }
      throw new HttpException(error.message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
