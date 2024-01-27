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
import { FaRegUser } from "react-icons/fa6";
import { Link } from "react-router-dom";
import React from "react";

const RightButtons = () => {
  const { isAuthenticated } = useAuth();
  const { i18n } = useTranslation();
  const { colorMode, toggleColorMode } = useColorMode();
  return (
    <>
      {isAuthenticated ? <AuthorizedButtons /> : <UnauthorizedButtons />}
      <IconButton
        variant={"unstyled"}
        aria-label={"Dark mode"}
        icon={colorMode === "dark" ? <Icon as={MdLightMode} /> : <Icon as={MdDarkMode} />}
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
          <MenuItem fontSize={"sm"} onClick={() => void i18n.changeLanguage("en")}>
            English
          </MenuItem>
          <MenuItem fontSize={"sm"} onClick={() => void i18n.changeLanguage("zh")}>
            中文
          </MenuItem>
        </MenuList>
      </Menu>
    </>
  );
};

const UnauthorizedButtons = () => {
  const { t } = useTranslation();
  return (
    <>
      <Button
        size={"sm"}
        variant={"unstyled"}
        onClick={() => void useAuth.getState().userManager.signinRedirect()}
      >
        {t("login")}
      </Button>
      <Link to={"/"}>{t("sign up")}</Link>
    </>
  );
};

const AuthorizedButtons = () => {
  const { user, signOut } = useAuth();
  const { t } = useTranslation();
  return (
    <>
      <Icon as={FaRegUser} fontSize={"xs"} />
      <Link to={"/"}>{user?.profile.preferred_username}</Link>
      <Button size={"sm"} variant={"unstyled"} onClick={signOut}>
        {t("sign out")}
      </Button>
    </>
  );
};

const SimpleLayout = () => {
  return (
    <>
      <HStack
        as="header"
        fontSize={"sm"}
        fontWeight={"semibold"}
        top={0}
        spacing={4}
        px={4}
        borderBottom={"1px"}
        h={"2.5rem"}
      >
        <Heading size={"md"}>Broccoli</Heading>
        <Spacer />
        {/*<RightButtons />*/}
      </HStack>
      <View />
    </>
  );
};

export default SimpleLayout;
