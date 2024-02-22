import { ForbiddenException, Injectable } from '@nestjs/common';
import { ProductDto } from './dto';
import { PrismaService } from 'src/prisma/prisma.service';
import { PrismaClientKnownRequestError } from '@prisma/client/runtime/library';

@Injectable()
export class ProductService {
  constructor(private prisma: PrismaService) {}

  async addProduct(dto: ProductDto) {
    try {
      const product = await this.prisma.product.create({
        data: {
          ...dto,
        },
      });

      return product;
    } catch (error) {
      if (error instanceof PrismaClientKnownRequestError) {
        if (error.code === 'P2002') {
          throw new ForbiddenException('Product already present.');
        } else if (error.code === 'P2003') {
          throw new ForbiddenException('Foreign key violation.');
        }
      }
      throw error;
    }
  }

  fetchAllProducts() {
    return ['product 1', 'product2'];
  }
}
