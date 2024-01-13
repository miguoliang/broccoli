import { defineStyle, defineStyleConfig } from "@chakra-ui/styled-system";

const baseStyle = defineStyle({
  mb: 0,
  marginEnd: 0,
});

export const formLabelTheme = defineStyleConfig({
  baseStyle,
});
