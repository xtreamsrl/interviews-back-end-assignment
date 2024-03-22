import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { Logger } from '@nestjs/common';
import { setupApp } from './config/setup';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  const logger = new Logger();
  const env: string | undefined = process.env.NODE_ENV;
  const { port, DNS } = setupApp(env);

  app.enableCors();
  await app.listen(port);
  logger.log(`server started on port ${DNS} (${env})`);
}
bootstrap();
