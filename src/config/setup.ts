export function setupApp(env: string | undefined) {
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

  return { port, DNS };
}
