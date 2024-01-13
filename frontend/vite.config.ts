import react from "@vitejs/plugin-react";
import { resolve } from "path";
import { defineConfig } from "vite";
import macrosPlugin from "vite-plugin-babel-macros";

// https://vitejs.dev/config/
export default defineConfig({
  base: "./",
  plugins: [
    macrosPlugin(),
    react(),
  ],
  build: {
    outDir: "build",
    sourcemap: process.env.NODE_ENV !== "production",
    minify: "esbuild",
  },
  server: {
    port: 3000, watch: {
      usePolling: true,
    },
  },
  define: {
    "process.env": `"{}"`,
  },
  resolve: {
    alias: [
      {
        find: /^assets(\/(.+)|$)/,
        replacement: resolve(__dirname, "src/assets/") + "$1",
      },
      {
        find: /^components(\/(.+)|$)/,
        replacement: resolve(__dirname, "src/components/") + "$1",
      },
      {
        find: /^configs(\/(.+)|$)/,
        replacement: resolve(__dirname, "src/configs/") + "$1",
      },
      {
        find: /^mock(\/(.+)|$)/,
        replacement: resolve(__dirname, "src/mock/") + "$1",
      },
      {
        find: /^services(\/(.+)|$)/,
        replacement: resolve(__dirname, "src/services/") + "$1",
      },
      {
        find: /^hooks(\/(.+)|$)/,
        replacement: resolve(__dirname, "src/hooks/") + "$1",
      },
      {
        find: /^views(\/(.+)|$)/,
        replacement: resolve(__dirname, "src/views/") + "$1",
      },
    ],
  },
});
