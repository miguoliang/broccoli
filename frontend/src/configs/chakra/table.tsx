import { tableAnatomy } from "@chakra-ui/anatomy";
import { createMultiStyleConfigHelpers } from "@chakra-ui/react";

const helpers = createMultiStyleConfigHelpers(tableAnatomy.keys);

// define the base component styles
const baseStyle = helpers.definePartsStyle({
  // define the part you're going to style
  th: {
    // this will style the MenuList component
    bg: "gray.50",
  },
});
// export the base styles in the component theme
export const tableTheme = helpers.defineMultiStyleConfig({ baseStyle });
