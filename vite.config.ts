import vue from "@vitejs/plugin-vue";
import Components from "unplugin-vue-components/vite";
import { VantResolver } from "unplugin-vue-components/resolvers";
import dns from "dns";
dns.setDefaultResultOrder("verbatim");

export default {
  plugins: [
    vue(),
    Components({
      resolvers: [VantResolver()],
    }),
  ],

  server: {
    port: 7070,
  },

  proxy: {
    "/": {
      target: "http://localhost:8081/api", // 代理地址，这里设置的地址会代替axios中设置的baseURL
      changeOrigin: true, // 如果接口跨域，需要进行这个参数配置
      ws: true,
      pathRewrite: {
        "^/": "",
      },
    },
  },
};
