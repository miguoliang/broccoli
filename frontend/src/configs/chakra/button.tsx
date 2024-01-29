import { defineStyle, defineStyleConfig } from "@chakra-ui/react";

const solid = defineStyle({
  borderRadius: "sm", // remove the border radius
});

const ghost = defineStyle({
  borderRadius: "sm", // remove the border radius
});

const sizes = defineStyle({
  "2xs": {
    fontSize: "xs",
    p: 0,
    minW: 5,
    h: 5,
  },
});

export const buttonTheme = defineStyleConfig({
  variants: { solid, ghost },
  sizes,
});
