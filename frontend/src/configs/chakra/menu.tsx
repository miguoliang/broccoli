import { menuAnatomy } from "@chakra-ui/anatomy";
import { createMultiStyleConfigHelpers } from "@chakra-ui/react";

const helpers = createMultiStyleConfigHelpers(menuAnatomy.keys);

// define the base component styles
const baseStyle = helpers.definePartsStyle({
  // define the part you're going to style
  list: {
    // this will style the MenuList component
    p: 2,
    borderRadius: "lg",
    minW: "5xs",
  },
  item: {
    borderRadius: "lg",
  },
  divider: {
    color: "gray.300",
  },
});
// export the base styles in the component theme
export const menuTheme = helpers.defineMultiStyleConfig({ baseStyle });
