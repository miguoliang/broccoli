import React, { cloneElement } from "react";
import {
  Avatar,
  Flex,
  HStack,
  Icon,
  Menu,
  MenuButton,
  MenuDivider,
  MenuItem,
  MenuList,
  Text,
} from "@chakra-ui/react";
import { Link } from "react-router-dom";
import { useAuth, useTimezone } from "hooks";
import { HiOutlineCog, HiOutlineLogout } from "react-icons/hi";
import { HiOutlineComputerDesktop } from "react-icons/hi2";

const menuList = [
  {
    label: "Dashboard",
    path: "/dashboard",
    icon: <Icon as={HiOutlineComputerDesktop} />,
  },
  {
    label: "Account",
    path: "/dashboard/account",
    icon: <Icon as={HiOutlineCog} />,
  },
];

const UserMenu = () => {
  const auth = useAuth();
  const timezone = useTimezone();
  const UserAvatar = (
    <HStack gap={2} px={2}>
      <Avatar size="sm" src="/img/avatars/thumb-1.jpg" />
      <Flex direction={"column"} alignItems={"start"}>
        <Text fontWeight={"bold"}>{auth.user?.profile.email}</Text>
        <Text fontSize={"sm"} color={"gray.500"} alignSelf={"end"}>
          {`Timezone is ${timezone.timezone}`}
        </Text>
      </Flex>
    </HStack>
  );

  return (
    <Menu autoSelect={false} placement="bottom-end">
      <MenuButton>{UserAvatar}</MenuButton>
      <MenuList>
        {menuList.map((item) => (
          <MenuItem
            as={Link}
            to={item.path}
            key={item.label}
            icon={cloneElement(item.icon, {
              fontSize: "lg",
              color: "gray.500",
            })}
          >
            {item.label}
          </MenuItem>
        ))}
        <MenuDivider />
        <MenuItem
          icon={
            <Icon as={HiOutlineLogout} fontSize={"lg"} color={"gray.500"} />
          }
          onClick={() => auth.signOut()}
        >
          Sign Out
        </MenuItem>
      </MenuList>
    </Menu>
  );
};

export default UserMenu;
