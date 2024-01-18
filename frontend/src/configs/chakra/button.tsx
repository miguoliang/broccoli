import { defineStyle, defineStyleConfig } from "@chakra-ui/react";

const solid = defineStyle({
  borderRadius: "sm", // remove the border radius
});

const ghost = defineStyle({
  borderRadius: "sm", // remove the border radius
});

export const buttonTheme = defineStyleConfig({
  variants: { solid, ghost },
});
