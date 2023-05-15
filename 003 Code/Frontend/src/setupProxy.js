const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function (app) {
  app.use(
    '/api',
    createProxyMiddleware({
      target: 'http://kjj.kjj.r-e.kr:8080',
      changeOrigin: true,
    }),
  );
  app.use(
    '/login/manager',
    createProxyMiddleware({
      target: 'http://kjj.kjj.r-e.kr:8080',
      changeOrigin: true,
    }),
  );
};