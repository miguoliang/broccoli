import { cardAnatomy } from "@chakra-ui/anatomy";
import { createMultiStyleConfigHelpers, cssVar } from "@chakra-ui/react";

const $padding = cssVar("card-padding");
const $radius = cssVar("card-radius");

const helpers = createMultiStyleConfigHelpers(cardAnatomy.keys);

const baseStyle = helpers.definePartsStyle(({ colorScheme: c }) => ({
  // define the part you're going to style
  container: {
    backgroundColor: `${c}.100`,
    _dark: {
      backgroundColor: `${c}.700`,
    },
  },
}));

const sizes = {
  xs: helpers.definePartsStyle({
    container: {
      [$radius.variable]: "radii.base",
      [$padding.variable]: "space.2",
    },
  }),
};

export const cardTheme = helpers.defineMultiStyleConfig({
  baseStyle,
  sizes,
  defaultProps: { colorScheme: "gray" },
});
