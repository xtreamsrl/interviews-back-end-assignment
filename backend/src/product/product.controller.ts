import { Controller, Post, Get, Body, Query } from '@nestjs/common';
import { ProductService } from './product.service';
import { ProductDto } from './dto';
import { Product } from '@prisma/client';
import {
  ApiTags,
  ApiResponse,
  ApiOperation,
  ApiQuery,
  ApiBody,
} from '@nestjs/swagger';

@ApiTags('product')
@Controller('product')
export class ProductController {
  constructor(private productService: ProductService) {}

  @Post('addproduct')
  @ApiOperation({ summary: 'Add a new product' })
  @ApiBody({ type: ProductDto })
  @ApiResponse({
    status: 201,
    description: 'The product has been successfully created.',
  })
  @ApiResponse({
    status: 400,
    description: 'The product has not been created.',
  })
  async addProduct(@Body() dto: ProductDto) {
    return this.productService.addProduct(dto);
  }

  @Get('fetchallproducts')
  @ApiOperation({ summary: 'Fetch all products' })
  @ApiResponse({
    status: 200,
    description: 'The products have been successfully fetched.',
  })
  fetchAllProducts() {
    return this.productService.fetchProductsCursorPagination({});
  }

  @Get('fetchallproductscursor')
  @ApiOperation({
    summary:
      'Fetch products with cursor-based pagination, useful to deal with large number of products.',
  })
  @ApiQuery({
    name: 'take',
    required: false,
    description: 'Number of products to take',
  })
  @ApiQuery({
    name: 'cursor',
    required: false,
    description:
      'Cursor (product id) indicating where to start fetching products',
  })
  @ApiResponse({
    status: 200,
    description: 'The products have been successfully fetched.',
  })
  async fetchProductsCursorPagination(
    @Query('take') take: string,
    @Query('cursor') cursor: string,
  ): Promise<Product[]> {
    return this.productService.fetchProductsCursorPagination({
      take: Number(take),
      cursor: { id: Number(cursor) },
    });
  }
}
