import { Controller, Post, Get, Body } from '@nestjs/common';
import { ProductService } from './product.service';
import { ProductDto } from './dto';

@Controller('product')
export class ProductController {
  constructor(private productService: ProductService) {}

  @Post('addproduct')
  addProduct(@Body() dto: ProductDto) {
    return this.productService.addProduct(dto)
  }

  @Get('fetchallproducts')
  fetchAllProducts() {
    return this.productService.fetchAllProducts()
  }
}
