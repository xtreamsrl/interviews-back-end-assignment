import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { Logger } from '@nestjs/common';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  const logger = new Logger();
  const env: string | undefined = process.env.NODE_ENV;
  let port: number;
  const DOMAIN: string | undefined =
    env === 'dev' ? 'localhost' : process.env.DOMAIN;

  const http: string = env === 'dev' ? 'http' : 'https';

  if (env === 'dev') {
    port = parseInt(process.env.PORT_DEV || '3000');
  } else if (env === 'prod') {
    port = parseInt(process.env.PORT_DEV || '3000');
  }
  const DNS = `${http}://${DOMAIN}:${port}`;

  await app.listen(port);
  logger.log(`server started on port ${DNS} (${env})`);
}
bootstrap();
