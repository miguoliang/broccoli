import { cardAnatomy } from "@chakra-ui/anatomy";
import { createMultiStyleConfigHelpers } from "@chakra-ui/react";

const helpers = createMultiStyleConfigHelpers(cardAnatomy.keys);

const baseStyle = helpers.definePartsStyle({
  // define the part you're going to style
  container: {
    backgroundColor: "gray.100",
    _dark: {
      backgroundColor: "gray.700",
    },
  },
});

export const cardTheme = helpers.defineMultiStyleConfig({ baseStyle });
