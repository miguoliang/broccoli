import { modalAnatomy as parts } from "@chakra-ui/anatomy";
import { createMultiStyleConfigHelpers } from "@chakra-ui/styled-system";

const helpers = createMultiStyleConfigHelpers(parts.keys);

const baseStyle = helpers.definePartsStyle({
  dialog: {
    bg: "chakra-body-bg",
  },
});

export const modalTheme = helpers.defineMultiStyleConfig({
  baseStyle,
});
