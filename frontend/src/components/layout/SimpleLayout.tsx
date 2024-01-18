import { UserMenu } from "components/ui";
import View from "views";
import { useAuth } from "hooks";
import {
  Button,
  Heading,
  HStack,
  Icon,
  IconButton,
  Menu,
  MenuButton,
  MenuItem,
  MenuList,
  Spacer,
  useColorMode,
} from "@chakra-ui/react";
import { useTranslation } from "react-i18next";
import { MdDarkMode, MdLanguage, MdLightMode } from "react-icons/md";

const SignInAndSignUp = () => {
  const { userManager, signOut } = useAuth();
  const { t, i18n } = useTranslation();
  const { colorMode, toggleColorMode } = useColorMode();
  return (
    <>
      <Button
        variant={"unstyled"}
        onClick={() => void userManager.signinRedirect()}
      >
        {t("login")}
      </Button>
      <Button variant={"unstyled"} onClick={() => signOut()}>
        {t("sign up")}
      </Button>
      <IconButton
        variant={"unstyled"}
        aria-label={"Dark mode"}
        icon={
          colorMode === "dark" ? (
            <Icon as={MdLightMode} />
          ) : (
            <Icon as={MdDarkMode} />
          )
        }
        onClick={() => toggleColorMode()}
      />
      <Menu>
        <MenuButton
          as={IconButton}
          aria-label={"Language"}
          variant={"unstyled"}
          icon={<Icon as={MdLanguage} />}
        ></MenuButton>
        <MenuList>
          <MenuItem
            fontSize={"sm"}
            onClick={() => void i18n.changeLanguage("en")}
          >
            English
          </MenuItem>
          <MenuItem
            fontSize={"sm"}
            onClick={() => void i18n.changeLanguage("zh")}
          >
            中文
          </MenuItem>
        </MenuList>
      </Menu>
    </>
  );
};

const SimpleLayout = () => {
  const isAuthenticated = useAuth((state) => state.isAuthenticated);

  return (
    <>
      <HStack
        as="header"
        top={0}
        spacing={4}
        px={4}
        borderBottom={"1px"}
      >
        <Heading size={"md"}>Broccoli</Heading>
        <Spacer />
        {isAuthenticated ? <UserMenu /> : <SignInAndSignUp />}
      </HStack>
      <View />
    </>
  );
};

export default SimpleLayout;
