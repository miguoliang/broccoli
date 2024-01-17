import { initReactI18next } from "react-i18next";
import { i18nextPlugin } from "translation-check";
import i18next from "i18next";
import en from "./locales/en.json";
import zh from "./locales/zh.json";

export const resources = {
  en,
  zh,
} as const;

void i18next.use(initReactI18next).use(i18nextPlugin).init({
  lng: "en",
  resources,
});
