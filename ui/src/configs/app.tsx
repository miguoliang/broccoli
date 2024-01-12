export default {
  appName: "KG Financial News",
  apiPrefix: window.location.origin + "/api",
  authenticatedEntryPath: "/dashboard",
  unAuthenticatedEntryPath: "/",
  enableMock: true,
  runtime: import.meta.env.MODE,
};
