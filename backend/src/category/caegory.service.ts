import { ForbiddenException, Injectable } from '@nestjs/common';
import { CategoryDto } from './dto';
import { PrismaService } from '../prisma/prisma.service';
import { PrismaClientKnownRequestError } from '@prisma/client/runtime/library';

@Injectable()
export class CategoryService {
  constructor(private prisma: PrismaService) {}

  async addCategory(dto: CategoryDto) {
    try {
      const category = await this.prisma.category.create({
        data: {
          ...dto,
        },
      });

      return category;
    } catch (error) {
      if (error instanceof PrismaClientKnownRequestError) {
        if (error.code === 'P2002') {
          throw new ForbiddenException('Category already present.');
        } else if (error.code === 'P2003') {
          throw new ForbiddenException('Foreign key violation.');
        }
      }
      throw error;
    }
  }

  fetchAllCategories() {
    return this.prisma.category.findMany();
  }
}
