import { modalAnatomy as parts } from "@chakra-ui/anatomy";
import { createMultiStyleConfigHelpers } from "@chakra-ui/styled-system";

const helpers = createMultiStyleConfigHelpers(parts.keys);

const baseStyle = helpers.definePartsStyle({
  dialog: {
    bg: "chakra-body-bg",
  },
  header: {
    px: 4,
    py: 2,
  },
  body: {
    px: 3,
    pt: 1,
    pb: 4,
  },
});

export const modalTheme = helpers.defineMultiStyleConfig({
  baseStyle,
});
