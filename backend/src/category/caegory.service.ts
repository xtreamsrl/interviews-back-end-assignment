import { ForbiddenException, Injectable } from '@nestjs/common';
import { CategoryDto } from './dto';
import { PrismaService } from 'src/prisma/prisma.service';
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

  // TODO: iplement

  fetchAllCategories() {
    return ['cat 1', 'cat 2'];
  }
}
