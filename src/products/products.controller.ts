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
  UseGuards,
} from '@nestjs/common';
import { ProductsService } from './products.service';
import { Product } from '../models/product.model';
import { CreateProductDTO, UpdateProductDTO } from './dtos/create-product.dto';
import mongoose from 'mongoose';
import { AuthGuard } from '../guard/auth.guard';
import { AdminGuard } from '../guard/admin.guard';

@Controller('products')
export class ProductsController {
  constructor(private productsService: ProductsService) {}

  @Get()
  async getProducts(
    @Query('page') page: number,
    @Query('limit') limit: number,
  ): Promise<Product[]> {
    const products = await this.productsService.getProducts(page, limit);
    if (!products)
      throw new HttpException('Products not found', HttpStatus.NOT_FOUND);
    return products;
  }

  @Get('/search')
  async searchProductsByName(
    @Query() queryParams: Record<string, any>,
  ): Promise<Product[]> {
    if (!queryParams) {
      return await this.productsService.getProducts();
    }
    const products =
      await this.productsService.getFilteredProducts(queryParams);
    return products;
  }

  @Get(':id')
  async getProductById(@Param('id') id: string): Promise<Product> {
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
  }

  @UseGuards(AuthGuard, AdminGuard)
  @Post()
  async addProduct(
    @Body() createProductDTOs: CreateProductDTO | CreateProductDTO[],
  ) {
    if (Array.isArray(createProductDTOs)) {
      const products = await Promise.all(
        createProductDTOs.map(async (createProductDTO) => {
          const product =
            await this.productsService.addProduct(createProductDTO);
          return product;
        }),
      );
      return products;
    } else {
      const product = await this.productsService.addProduct(createProductDTOs);
      return product;
    }
  }

  @UseGuards(AuthGuard, AdminGuard)
  @Put(':id')
  async updateProduct(
    @Param('id') id: string,
    @Body() updateProductDTO: UpdateProductDTO,
  ): Promise<Product> {
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
  }

  @UseGuards(AuthGuard, AdminGuard)
  @Delete(':id')
  async deleteProduct(@Param('id') id: string): Promise<Product> {
    const deletedProduct = await this.productsService.deleteProduct(id);
    if (!deletedProduct) throw new NotFoundException('Product does not exist!');

    return deletedProduct;
  }

  @UseGuards(AuthGuard, AdminGuard)
  @Delete()
  async deleteProducts(@Body() ids: string[]): Promise<Product[]> {
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
  }
}
