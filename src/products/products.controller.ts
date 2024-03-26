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
import { ProductsService } from './products.service';
import { CreateProductDTO, UpdateProductDTO } from './dtos/create-product.dto';
import mongoose from 'mongoose';
import { AuthGuard } from '../guard/auth.guard';
import { AdminGuard } from '../guard/admin.guard';
import { Response } from 'express';

@Controller('products')
export class ProductsController {
  constructor(private productsService: ProductsService) {}

  @Get()
  async getProducts(
    @Query('page') page: number,
    @Query('limit') limit: number,
    @Res() res: Response,
  ) {
    const { products, total, totalPages } =
      await this.productsService.getProducts(page, limit);
    if (!products)
      throw new HttpException('Products not found', HttpStatus.NOT_FOUND);

    return res.status(HttpStatus.OK).json({
      message: 'get products successfully',
      data: products,
      ...(page &&
        limit && {
          totalCount: total,
          currentPage: page,
          totalPage: totalPages,
        }),
    });
  }

  @Get('/search')
  async searchProductsByName(
    @Query() queryParams: Record<string, any>,
    @Res() res: Response,
  ) {
    if (!queryParams) {
      return await this.productsService.getProducts();
    }
    const products =
      await this.productsService.getFilteredProducts(queryParams);
    return res
      .status(HttpStatus.OK)
      .json({ message: 'products found successfully', data: products });
  }

  @Get(':id')
  async getProductById(@Param('id') id: string, @Res() res: Response) {
    const isValid = mongoose.Types.ObjectId.isValid(id);
    if (!isValid)
      throw new HttpException(
        'Invalid mongoose Id, Product not found',
        HttpStatus.BAD_REQUEST,
      );
    const product = await this.productsService.getProductById(id);
    if (!product) {
      throw new HttpException('Product not found', HttpStatus.NOT_FOUND);
    }

    return res
      .status(HttpStatus.OK)
      .json({ message: 'product found successfully', data: product });
  }

  @UseGuards(AuthGuard, AdminGuard)
  @Post()
  async addProduct(
    @Body() createProductDTOs: CreateProductDTO | CreateProductDTO[],
    @Res() res: Response,
  ) {
    if (Array.isArray(createProductDTOs)) {
      const products = await Promise.all(
        createProductDTOs.map(async (createProductDTO) => {
          const product =
            await this.productsService.addProduct(createProductDTO);
          return product;
        }),
      );

      return res
        .status(HttpStatus.OK)
        .json({ message: 'products added successfully', data: products });
    } else {
      const product = await this.productsService.addProduct(createProductDTOs);

      return res
        .status(HttpStatus.OK)
        .json({ message: 'product added successfully', data: product });
    }
  }

  @UseGuards(AuthGuard, AdminGuard)
  @Put(':id')
  async updateProduct(
    @Param('id') id: string,
    @Body() updateProductDTO: UpdateProductDTO,
    @Res() res: Response,
  ) {
    const isValid = mongoose.Types.ObjectId.isValid(id);
    if (!isValid)
      throw new HttpException(
        'Invalid mongoose Id, Product not found to update',
        HttpStatus.BAD_REQUEST,
      );
    const updatedProduct = await this.productsService.updateProduct(
      id,
      updateProductDTO,
    );
    if (!updatedProduct) {
      throw new NotFoundException('Product does not exist!');
    }
    return res
      .status(HttpStatus.OK)
      .json({ message: 'product updated successfully', data: updatedProduct });
  }

  @UseGuards(AuthGuard, AdminGuard)
  @Delete(':id')
  async deleteProduct(@Param('id') id: string, @Res() res: Response) {
    const deletedProduct = await this.productsService.deleteProduct(id);
    if (!deletedProduct) throw new NotFoundException('Product does not exist!');

    return res
      .status(HttpStatus.OK)
      .json({ message: 'product deleted successfully', data: deletedProduct });
  }

  @UseGuards(AuthGuard, AdminGuard)
  @Delete()
  async deleteProducts(@Body() ids: string[], @Res() res: Response) {
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
    return res.status(HttpStatus.OK).json({
      message: 'products deleted successfully',
      data: deletedProducts,
    });
  }
}
