import { ForbiddenException, Injectable } from '@nestjs/common';
import { ProductDto } from './dto';
import { PrismaService } from '../prisma/prisma.service';
import { PrismaClientKnownRequestError } from '@prisma/client/runtime/library';
import { Product, Prisma } from '@prisma/client';

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

  async fetchProductsCursorPagination(params: {
    take?: number;
    cursor?: Prisma.ProductWhereUniqueInput | null;
  }): Promise<Product[]> {
    const { take, cursor } = params;
    const options: Prisma.ProductFindManyArgs = {
      skip: 0,
    };

    if (!isNaN(take) && take !== undefined) {
      options.take = take;
    }

    if (cursor && cursor.id) {
      options.cursor = cursor;
      options.skip = 1;
    }

    return this.prisma.product.findMany(options);
  }
}
