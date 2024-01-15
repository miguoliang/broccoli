export default {
  broccoli: {
    output: {
      mode: "split",
      target: "src/gens/backend/api.ts",
      schemas: "src/gens/backend/model",
      client: "react-query",
      mock: true,
      baseUrl: "/api/v1",
    },
    input: {
      target: "./test.yml",
      // "../backend/build/classes/java/main/META-INF/swagger/broccoli-1.0.yml",
    },
    afterAllFilesWrite: "prettier --write",
  },
};
