import React, { PropsWithChildren } from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "hooks";
import intersection from "lodash-es/intersection";
import { Stack } from "@chakra-ui/react";

export type AuthorityGuardType = PropsWithChildren<{
  authority?: string[];
}>;

export const AuthorityGuard = (props: AuthorityGuardType) => {
  const { authority = [], children } = props;

  const scopes = useAuth((state) => state.user?.scopes ?? []);
  const roleMatched =
    authority.length === 0 || intersection(scopes, authority).length > 0;

  return (
    <Stack className="h-full">
      {roleMatched ? children : <Navigate to="/access-denied" />}
    </Stack>
  );
};
