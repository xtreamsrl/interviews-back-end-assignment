import { Test } from '@nestjs/testing';
import { AppModule } from '../src/app.module';
import { INestApplication, ValidationPipe } from '@nestjs/common';
import { PrismaService } from '../src/prisma/prisma.service';
import { CategoryDto } from 'src/category/dto';
import * as pactum from 'pactum';
import { ProductDto } from 'src/product/dto';

describe('App e2e', () => {
  let app: INestApplication;
  let prisma: PrismaService;
  beforeAll(async () => {
    const moduleRef = await Test.createTestingModule({
      imports: [AppModule],
    }).compile();
    app = moduleRef.createNestApplication();
    app.useGlobalPipes(
      new ValidationPipe({
        whitelist: true,
      }),
    );
    await app.init();
    await app.listen(3333);

    prisma = app.get(PrismaService);
    await prisma.cleanDb();
    pactum.request.setBaseUrl('http://localhost:3333');
  });
  afterAll(() => {
    app.close();
  });

  describe('category', () => {
    describe('fetch all empty', () => {
      it('should get', () => {
        return pactum
          .spec()
          .get('/category/fetchallcategories')
          .expectStatus(200)
          .expectBody([]);
      });
    });

    const dto: CategoryDto = {
      name: 'TestCategory',
    };
    describe('add category', () => {
      it('should add', () => {
        return pactum
          .spec()
          .post('/category/addcategory')
          .withBody(dto)
          .expectStatus(201)
          .stores('categoryId', 'id');
      });

      it('should throw if name empty', () => {
        return pactum
          .spec()
          .post('/category/addcategory')
          .withBody({})
          .expectStatus(400);
      });
    });

    describe('fetch all categories', () => {
      it('should get', () => {
        return pactum
          .spec()
          .get('/category/fetchallcategories')
          .expectStatus(200)
          .expectJsonLength(1)
          .expectJson('0', {
            id: '$S{categoryId}',
            name: dto.name,
          });
      });
    });
  });

  describe('product', () => {
    describe('fetch all empty', () => {
      it('should get', () => {
        return pactum
          .spec()
          .get('/product/fetchallproducts')
          .expectStatus(200)
          .expectBody([]);
      });
    });

    const dto: ProductDto = {
      name: 'Example Product Name',
      serialNo: 'AB456',
      imageURL: 'https://example.com/image.jpg',
      price: 19.99,
      quantity: 100,
      categoryId: 1,
    };

    describe('addproduct', () => {
      it('should add', () => {
        return pactum
          .spec()
          .post('/product/addproduct')
          .withBody({ ...dto, categoryId: '$S{categoryId}' })
          .expectStatus(201);
      });

      it('should throw if empty', () => {
        return pactum
          .spec()
          .post('/product/addproduct')
          .withBody({})
          .expectStatus(400);
      });
    });

    describe('fetch all products', () => {
      it('should get', () => {
        return pactum
          .spec()
          .get('/product/fetchallproducts')
          .expectStatus(200)
          .expectJsonLength(1)
          .expectJsonLike('0', {
            serialNo: dto.serialNo,
            name: dto.name,
          });
      });
    });
  });
});
