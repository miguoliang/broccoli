/** @type {import("eslint").Linter.Config } */
module.exports = {
  root: true,
  ignorePatterns: [
    "node_modules",
    "dist",
    "tailwind.config.ts",
    "postcss.config.cjs",
    ".eslintrc.cjs",
    "orval.config.ts",
    "src/gens/**/*",
  ],
  parser: "@typescript-eslint/parser",
  parserOptions: {
    project: "./tsconfig.eslint.json",
  },
  extends: [
    "eslint:recommended",
    "plugin:@typescript-eslint/recommended-type-checked",
    "plugin:@tanstack/eslint-plugin-query/recommended",
  ],
  plugins: ["@typescript-eslint", "@tanstack/query", "prettier"],
  overrides: [
    {
      files: ["*.js"],
      extends: ["plugin:@typescript-eslint/disable-type-checked"],
    },
  ],
  rules: {
    "@tanstack/query/exhaustive-deps": "error",
    "@tanstack/query/no-rest-destructuring": "warn",
    "@tanstack/query/stable-query-client": "error",
  }
};
