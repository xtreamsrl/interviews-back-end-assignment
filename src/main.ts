import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { Logger } from '@nestjs/common';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  const logger = new Logger(); // initialize nest logger
  let port;

  const env = process.env.NODE_ENV;
  const http = env === 'prod' ? 'https' : 'http';
  const DOMAIN = env === 'dev' && 'localhost';

  console.log(port);
  if (process.env.NODE_ENV === 'dev') {
    port = process.env.PORT_DEV;
  } else if (process.env.NODE_ENV === 'prod') {
    port = process.env.PORT_PROD; //currently there is no production environment
  }
  const DNS = `${http}://${DOMAIN}:${port}`;

  await app.listen(port);
  logger.log(`server started on port ${DNS} (${env})`);
}
bootstrap();
