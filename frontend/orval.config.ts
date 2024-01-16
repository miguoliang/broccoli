export default {
  broccoli: {
    output: {
      mode: "split",
      target: "src/gens/backend/api.ts",
      schemas: "src/gens/backend/model",
      client: "react-query",
      mock: true,
      baseUrl: "/api/v1",
      prettier: true,
      tslint: true,
      override: {
        urlEncodeParameters: true,
        useNativeEnums: true,
        useTypeOverInterfaces: true,
        useDeprecatedOperations: false,
        paramsSerializerOptions: {
          qs: {
            arrayFormat: "repeat",
          },
        },
      },
    },
    input: {
      target:
        "../backend/build/classes/java/main/META-INF/swagger/broccoli-1.0.yml",
    },
  },
};
