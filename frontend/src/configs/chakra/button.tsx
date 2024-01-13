import { defineStyle, defineStyleConfig } from "@chakra-ui/react";

const dropdownMenuItem = defineStyle({
  bg: "transparent",
  _hover: { bg: "gray.100" },
  fontSize: 1,
  fontWeight: "normal",
  display: "flex",
  justifyContent: "flex-start",
  textAlign: "left",
  px: 2,
  minW: "5xs",
});

const navigationRootMenuItem = defineStyle({
  bg: "transparent",
  _hover: { bg: "gray.200" },
  display: "flex",
  justifyContent: "flex-start",
  textAlign: "left",
  px: 2,
  width: "full",
});

const paginationButton = defineStyle({
  bg: "transparent",
});

export const buttonTheme = defineStyleConfig({
  variants: { dropdownMenuItem, navigationRootMenuItem, paginationButton },
});
