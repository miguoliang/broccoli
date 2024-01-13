import { cardAnatomy as parts } from "@chakra-ui/anatomy";
import { createMultiStyleConfigHelpers } from "@chakra-ui/styled-system";

const helpers = createMultiStyleConfigHelpers(parts.keys);

const variants = {
  elevated: helpers.definePartsStyle({
    container: {
      shadow: "none",
      borderWidth: "1px",
      borderColor: "gray.200",
    },
  }),
};

export const cardTheme = helpers.defineMultiStyleConfig({
  variants,
});
