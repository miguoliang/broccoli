import { defineStyleConfig } from "@chakra-ui/react";
import { defineStyle } from "@chakra-ui/styled-system";

// define the base component styles
const navigationRootMenuItem = defineStyle({
  borderRadius: "lg",
  p: 2,
  fontSize: "md",
});

// export the component theme
export const tooltipTheme = defineStyleConfig({
  variants: { navigationRootMenuItem },
});
