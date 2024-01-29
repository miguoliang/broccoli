import { Suspense } from "react";
import { protectedRoutes, publicRoutes } from "configs";
import { Navigate, Route, Routes } from "react-router-dom";
import { HTMLChakraProps, Spinner } from "@chakra-ui/react";
import { AppRoute, AuthorityGuard, ProtectedRoute, PublicRoute } from "components/route";

const AllRoutes = () => {
  return (
    <Routes>
      <Route path={"/dashboard"} element={<ProtectedRoute />}>
        {protectedRoutes.map((route, index) => (
          <Route
            key={route.path}
            path={route.path}
            element={
              <AuthorityGuard authority={route.authority}>
                <AppRoute component={route.component} {...route.meta} />
              </AuthorityGuard>
            }
          />
        ))}
      </Route>
      <Route path="/" element={<PublicRoute />}>
        {publicRoutes.map((route) => (
          <Route
            key={route.key}
            path={route.path}
            element={<AppRoute component={route.component} {...route.meta} />}
          />
        ))}
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
};

const Views = (props: HTMLChakraProps<"div">) => {
  return (
    <Suspense fallback={<Spinner />}>
      <AllRoutes {...props} />
    </Suspense>
  );
};

export default Views;
