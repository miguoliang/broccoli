/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_OIDC_SOCPES: string;
  readonly VITE_OIDC_CLIENT_ID: string;
  readonly VITE_OIDC_REDIRECT_URI: string;
  readonly VITE_OIDC_SIGN_UP_URL: string;
  readonly VITE_OIDC_AUTHORITY: string;
  readonly VITE_OIDC_CLIENT_SECRET: string;
  readonly VITE_OIDC_SIGN_OUT_URL: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
